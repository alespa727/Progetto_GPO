const widths = ["w-32", "w-48", "w-64", "w-40", "w-56", "w-36", "w-52"];

export function MessaggioSkeleton({ showImage }: { showImage: boolean }) {
  return (
    <div className="rounded-r-[var(--radius)] px-6 mr-4 flex items-center animate-pulse">
      <div className="flex flex-col gap-2 p-1 w-full">
        <div className="flex gap-2 items-center">
          <div className="h-3 w-8 bg-white/10 rounded" />
          <div className="h-3 w-20 bg-white/10 rounded" />
          <div className={`h-3 ${widths[Math.floor(Math.random() * widths.length)]} bg-white/10 rounded`} />
        </div>
        {showImage && (
          <div className="h-56 w-80 bg-white/10 rounded-[var(--radius)]" />
        )}
      </div>
    </div>
  );
}