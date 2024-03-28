g++ -fPIC -shared -o ../../native.dll -I "%JAVA_HOME%\include\win32" -I "%JAVA_HOME%\include" native.cpp ./GroupProject/PicassoLWD.FastSimulation.Native.dll
if not exist "..\..\PicassoLWD.FastSimulation.Native.dll" (
    copy ".\GroupProject\PicassoLWD.FastSimulation.Native.dll" "..\..\"
)
