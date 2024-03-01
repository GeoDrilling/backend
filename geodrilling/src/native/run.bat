g++ -fPIC -shared -o ../../native.dll -I "C:\Users\1\.jdks\corretto-17.0.4.1\include\win32" -I "C:\Users\1\.jdks\corretto-17.0.4.1\include" native.cpp ./GroupProject/PicassoLWD.FastSimulation.Native.dll
if not exist "..\..\PicassoLWD.FastSimulation.Native.dll" (
    copy ".\GroupProject\PicassoLWD.FastSimulation.Native.dll" "..\..\"
)
