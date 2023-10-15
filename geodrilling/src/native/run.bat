cd FakePicasso
g++ PicassoLWD.FastSimulation.Native.cpp -c -std=c++20 -fPIC
g++ PicassoLWD.FastSimulation.Native.o -shared -o ../PicassoLWD.FastSimulation.Native.dll
cd ..
g++ -fPIC -shared -o ../../native.dll -I"C:\Program Files\Java\jdk-11.0.16.1\include\win32" -I"C:\Program Files\Java\jdk-11.0.16.1\include" native.cpp ./PicassoLWD.FastSimulation.Native.dll
move .\native.dll ..\..\
move .\PicassoLWD.FastSimulation.Native.dll ..\..\
