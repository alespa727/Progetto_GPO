package it.edu.maxplanck.gpoProject_Server.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import it.edu.maxplanck.gpoProject_Server.authentication.AuthenticationService;
import it.edu.maxplanck.gpoProject_Server.database.model.User;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriend;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestFriends;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestStatus;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriend;
import it.edu.maxplanck.gpoProject_Server.dto.response.ResponseFriends;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataException;
import it.edu.maxplanck.gpoProject_Server.exceptions.DataExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/admin")
public class AdminApiController extends BasicApiRestController {

	private final RequestMappingHandlerMapping handlerMapping;
	
	public AdminApiController(DatabaseService databaseService, AuthenticationService authenticationService, @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
		super(databaseService, authenticationService);
		this.handlerMapping = handlerMapping;
		// TODO Auto-generated constructor stub
	}
	
	private Map<String, Object> extractRequest(HandlerMethod method) {

	    Map<String, Object> request = new HashMap<>();

	    for (MethodParameter param : method.getMethodParameters()) {

	        if (param.hasParameterAnnotation(RequestBody.class)) {

	            Class<?> bodyClass = param.getParameterType();

	            request.put("type", bodyClass.getSimpleName());
	            request.put("fields", extractFields(bodyClass));
	        }
	    }

	    return request;
	}
	
	private Map<String, Object> extractResponse(HandlerMethod method) {

	    Map<String, Object> response = new HashMap<>();

	    Method javaMethod = method.getMethod();
	    Class<?> returnType = javaMethod.getReturnType();

	    // niente output
	    if (returnType.equals(Void.TYPE)) {
	        return response;
	    }

	    // ResponseEntity<T>
	    if (ResponseEntity.class.isAssignableFrom(returnType)) {

	        Type generic = javaMethod.getGenericReturnType();

	        if (generic instanceof ParameterizedType parameterized) {
	            Type actual = parameterized.getActualTypeArguments()[0];

	            if (actual instanceof Class<?> clazz && !clazz.equals(Void.class)) {
	                response.put("type", clazz.getSimpleName());
	                response.put("fields", extractFields(clazz));
	            }
	        }

	        return response;
	    }

	    // ritorno diretto
	    response.put("type", returnType.getSimpleName());
	    response.put("fields", extractFields(returnType));

	    return response;
	}
	
	private List<Map<String, String>> extractFields(Class<?> clazz) {

	    List<Map<String, String>> fields = new ArrayList<>();

	    for (Field field : clazz.getDeclaredFields()) {

	        // salta static / transient
	        if (Modifier.isStatic(field.getModifiers()) ||
	            Modifier.isTransient(field.getModifiers())) {
	            continue;
	        }

	        Map<String, String> fieldInfo = new HashMap<>();
	        fieldInfo.put("name", field.getName());
	        fieldInfo.put("type", field.getType().getSimpleName());

	        fields.add(fieldInfo);
	    }

	    return fields;
	}
	
	@GetMapping("/endpoints")
    public List<Map<String, Object>> listEndpoints(HttpServletRequest request, HttpServletResponse response) {
        
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		User u = this.databaseService.findUser(id);
		if(!u.isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		/*
		 * Ritorna endpoints
		*/
		return handlerMapping.getHandlerMethods().entrySet().stream()
		        .filter(e ->
		            e.getValue()
		             .getBeanType()
		             .isAnnotationPresent(RestController.class)
		        )
		        .map(entry -> {

		            RequestMappingInfo info = entry.getKey();
		            HandlerMethod method = entry.getValue();

		            Map<String, Object> map = new HashMap<>();

		            map.put("path", info.getPatternValues());
		            map.put("httpMethods", info.getMethodsCondition().getMethods());

		            Map<String, Object> requestInfo = extractRequest(method);
		            if (!requestInfo.isEmpty()) {
		                map.put("request", requestInfo);
		            }

		            Map<String, Object> responseInfo = extractResponse(method);
		            if (!responseInfo.isEmpty()) {
		                map.put("response", responseInfo);
		            }

		            return map;
		        })
		        .toList();
    }
	
	/**
	 * Aggiorna i dati dello status
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("status")
	public ResponseEntity<?> status(HttpServletRequest request, HttpServletResponse response, @RequestBody() RequestStatus status){
		
		/*
		 * Autentificazione
		 */
		int id = this.authenticationService.authenticate(request, response);
		
		User u = this.databaseService.findUser(id);
		if(!u.isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		/*
		 * Update database
		*/
		this.databaseService.updateStatusUser(u, status.isOnline());
		
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Ottiene le immagini profilo degli utenti richiesti
	 * @param request
	 * @param response
	 * @param body
	 * @return
	 */
	@GetMapping("profiles")
	public ResponseEntity<?> users(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestFriends body){
		
		int id = this.authenticationService.authenticate(request, response);
		
		User u = this.databaseService.findUser(id);
		if(!u.isAdmin()) throw new DataException(DataExceptions.DATA_FORBIDDEN);
		
		List<ResponseFriend> usersImagePaths = new ArrayList<ResponseFriend>();
		for(RequestFriend r : body.getUsers()) {
			User utente = null;
			try{
				utente = this.databaseService.findUser(r.getUsername());
			} catch(Exception e) {
				e.printStackTrace();
			}
			usersImagePaths.add(new ResponseFriend(r.getUsername(), this.findImage(utente)));
		}
		
		ResponseFriends users = new ResponseFriends(usersImagePaths);
		
		return ResponseEntity.ok().body(users);
	}
}
