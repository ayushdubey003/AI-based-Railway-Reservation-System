#include <stdio.h>

int main(int argc, char *argv[])
{
   if(argc > 1)
   {
      int i;
      for(i = 0; i < argc; i++)
         printf("%s ", argv[i]);
      printf("\n");
   }
   else
      printf("%s: No argument is provided!\n", argv[0]);
   return(0);
}