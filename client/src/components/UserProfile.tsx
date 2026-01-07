import { useUser } from "../context/UserProvider";


function UserProfile({}) {
  const user = useUser();

  return (
    <div className="profile p-2.5 flex items-center">
      <div className="pfp w-16 h-16 bg-gray-400 rounded-full"></div>
      <h1 className="flex-1 pl-3 font-medium text-2xl">
          {user?.username}
      </h1>
    </div>
    );
}

export default UserProfile;
