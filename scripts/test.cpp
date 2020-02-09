// C++ implementation to read 
// file word by word 
#include <bits/stdc++.h> 
using namespace std; 
  
// driver code 
int main() 
{ 
    // filestream variable file 
    fstream file; 
    string word, t, q, filename; 
  
    // filename of the file 
    filename = "file.txt"; 
  
    // opening file 
    file.open(filename.c_str()); 
  
    // extracting words from the file 
    while (file >> word) 
    { 
        // displaying content 
        cout << word << endl; 
    } 
  
    return 0; 
} 