import os
import sys

argc = len(sys.argv)
argv =  sys.argv

if argc > 2:
   cmd = './'
   for i in range(1,argc):
      cmd = cmd + argv[i] + ' '
   if os.path.isfile(argv[1]):
      # print('Python script: ', cmd)
      os.system(cmd)
   else:
      print('Binary file does not exist')
      bin = 'gcc -o ' + argv[1] + ' '+ argv[1] + '.c'
      print(bin)
      os.system(bin)
      if os.path.isfile(argv[1]):
         os.system(cmd)
      else:
         print('Binary source does not exist')
         exit(0)
else:
   print('USAGE: python3.4', argv[0], " BINARY_FILE INPUT_ARGS");
   exit(0)