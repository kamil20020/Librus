import { createContext, useContext, useState } from "react";

interface LoadingProps {
  numberOfLoads: number;
}

interface LoadingState {
  props: LoadingProps;
  isLoading: () => boolean;
  setIsLoading: (isLoading: boolean) => void;
}

const LoadingContext = createContext<LoadingState | null>(null);

const LoadingProvider = (props: { children: React.ReactNode }) => {

  const [loadingProps, setLoadingProps] = useState<LoadingProps>({
    numberOfLoads: 0,
  });

  const isLoading = (): boolean => {
    return loadingProps.numberOfLoads > 0;
  }

  const setIsLoading = (isLoadingValue: boolean) => {
    if (isLoadingValue) {
      setLoadingProps(loadingProps => ({
        numberOfLoads: loadingProps.numberOfLoads + 1,
      }));

      return;
    }

    setLoadingProps(loadingProps => ({
      numberOfLoads: loadingProps.numberOfLoads - 1,
    }));
  };

  return (
    <LoadingContext.Provider value={{ props: loadingProps, isLoading, setIsLoading }}>
        <>
            {props.children}
            {isLoading() &&
              <div id="loading-info"
                style={{
                    position: "absolute",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    backgroundColor: "rgba(0, 0, 0, 0.4)",
                    color: 'var(--secondary-color)',
                    fontSize: 42,
                    fontWeight: "bold",
                    top: 0,
                    left: 0,
                    width: "100%",
                    height: "100%",
                    zIndex: 100
                }}
               >
                Ładowanie
              </div>
            }
        </>
    </LoadingContext.Provider>
  );
};

export const useLoading = () => {

    const context = useContext(LoadingContext)

    if(!context){
        throw new Error("Loading context is not set");
    }

    return context;
}

export default LoadingProvider;
