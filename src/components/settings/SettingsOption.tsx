function Option({ name = "", isActive = false, onClick = () => {}, icon }: {
  name: string;
  isActive?: boolean;
  onClick?: () => void;
  icon?: React.ReactNode;
}) {
  return (
    <div
      onClick={onClick}
      className={`flex items-center gap-3 w-full px-3 py-2 rounded-lg cursor-pointer transition-all duration-150
        ${isActive
          ? "bg-white/15 text-white"
          : "text-white/50 hover:bg-white/10 hover:text-white"
        }`}
    >
      <div className="w-8 h-8 flex items-center justify-center rounded-md shrink-0">
        {icon ?? <div className="w-full h-full bg-white/20 rounded-md" />}
      </div>
      <p className="text-sm font-medium">{name}</p>
    </div>
  );
}
export default Option;