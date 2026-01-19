import placeholder from "../../assets/placeholder.png";

export function ProfilePicture({ className }: { className: string }) {
    return (
        <div className={`rounded-full overflow-hidden ${className}`}>
            <img
                src={placeholder}
                alt=""
                className="w-full h-full object-cover"
            />
        </div>
    );
}