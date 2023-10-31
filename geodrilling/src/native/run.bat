cd FakePicasso
g++ PicassoLWD.FastSimulation.Native.cpp -c -std=c++20 -fPIC
g++ PicassoLWD.FastSimulation.Native.o -shared -o ../PicassoLWD.FastSimulation.Native.dll
cd ..
g++ -fPIC -shared -o ../../native.dll -I "%JAVA_HOME%\include\win32" -I "%JAVA_HOME%\include" native.cpp ./PicassoLWD.FastSimulation.Native.dll
move .\PicassoLWD.FastSimulation.Native.dll ..\..\
