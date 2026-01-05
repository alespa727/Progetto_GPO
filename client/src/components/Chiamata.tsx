function Chiamata({isActive}:{isActive: boolean}) {
  if(!isActive) return;
   return (
   <div className="w-full h-1/2 bg-red-500 absolute top-[49px]"></div>
  );
}


export default Chiamata