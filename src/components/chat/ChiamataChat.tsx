import { ReactNode, useCallback, useEffect, useRef, useState } from "react";


interface DraggablePopupProps {
  isOpen: boolean; // Whether the popup is open or not
  onClose: () => void; // Function to close the popup
  children: ReactNode; // Content to be displayed inside the popup
}

const ChiamataChat: React.FC<DraggablePopupProps> = ({isOpen,onClose,children,}) => {
  if (!isOpen) return null;

  // State to determine if the popup is being dragged
  const [isDragging, setIsDragging] = useState(false);

  // State to keep track of the popup's position
  const [position, setPosition] = useState<{ x: number; y: number }>({
    x: 0,
    y: 0,
  });

  // Ref to store the initial mouse position when dragging starts
  const startPos = useRef<{ x: number; y: number }>({ x: 0, y: 0 });

  // Ref to store the popup element
  const popupRef = useRef<HTMLDivElement | null>(null)
  const windowRef = useRef<HTMLDivElement | null>(null)

  const getPopupPoints = () => {
    if (!popupRef.current) return null;

    const rect = popupRef.current.getBoundingClientRect();

    return {
        topLeft: { x: rect.left, y: rect.top },
        topRight: { x: rect.right, y: rect.top },
        bottomRight: { x: rect.right, y: rect.bottom },
        bottomLeft: { x: rect.left, y: rect.bottom },
    };
};

  const getWindowRef = () => {
    if (!windowRef.current) return null;

    const rect = windowRef.current.getBoundingClientRect();
    
    return {
        topLeft: { x: rect.left, y: rect.top },
        topRight: { x: rect.right, y: rect.top },
        bottomRight: { x: rect.right, y: rect.bottom },
        bottomLeft: { x: rect.left, y: rect.bottom },
    };
};
    

  const onMouseMove = useCallback(
    (e: MouseEvent) => {
      if (!isDragging) return;
      console.log(e.clientX - startPos.current.x, e.clientY - startPos.current.y);
      setPosition({
        x: e.clientX - startPos.current.x,
        y: e.clientY - startPos.current.y,
      });
            
        const popupPoints = getPopupPoints();
        
        if (!popupPoints) return;
    },
    [isDragging]
  );

  // Function to handle the end of a drag event
  const onMouseUp = () => {
    setIsDragging(false);
  };

  // Function to handle the start of a drag event
  const onMouseDown = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
    setIsDragging(true);
    startPos.current = { x: e.clientX - position.x, y: e.clientY - position.y };
  };

   // Effect to add and clean up event listeners for dragging
  useEffect(() => {
    window.addEventListener("mousemove", onMouseMove);
    window.addEventListener("mouseup", onMouseUp);
    return () => {
      window.removeEventListener("mousemove", onMouseMove);
      window.removeEventListener("mouseup", onMouseUp);
    };
  }, [onMouseMove]);

  // Reset position when the popup is closed
  useEffect(() => {
    if (!isOpen) {
      setPosition({ x: 0, y: 0 });
    }
  }, [isOpen]);
  

  if (!isOpen) return null;
  return (
    <div ref={windowRef} className="fixed top-0 left-0 w-full h-full bg-black flex justify-center items-center" onClick={onClose}>
      <div 
        onMouseDown={onMouseDown}
        style={{ transform: `translate(${position.x}px, ${position.y}px)` }} 
        ref={popupRef} onClick={(e) => e.stopPropagation()} 
        className="bg-white rounded-[8px] flex text-black h-100 w-100 relative">
            {children}
        </div>
    </div>
  );
};

export default ChiamataChat;