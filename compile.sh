g++ -O3 -Wall -shared -std=c++11 -fPIC         \
  -I/usr/local/include                         \
  -I/usr/include/python3.6/                    \
  -L/usr/local/lib                             \
  -o test1`python3-config --extension-suffix`  \
  bind.cc
