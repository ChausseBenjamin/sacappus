/* matAdd.c
 * ----------------------------------------------------------------------------
 * Auteurs:
 *    Benjamin Chausse  - chab1704
 *    Guillaume Malgorn - malg1503
 * Description:
 *     Fonction multipliant deux matrices ensemble
 *     ainsi que ses tests de validation.
 * Date: 2022-10-04
 * ----------------------------------------------------------------------------
 */

#include <stdio.h>

#define n 3


void matprint(int *arr, int w, int h){
  int i, j;
  for (i = 0; i < w; i++) {
    for (j = 0; j < h; j++) {
      printf("%3d ", *((arr+i*h) + j));
    }
    printf("\n");
  }
}

int *matMult(int *ma, int *mb){
  int ha = n;
  int c = n;
  int wb = n;
  int res[ha][wb];
  for (int i=0;i<ha;i++){
    for (int j=0;j<wb;j++){
      int cell = 0;
      for (int k=0;k<c;k++){
        cell += *((ma+i*c)+k) * *((mb+wb*k)+j);
      }
      res[i][j] = cell;
    }
  }
  return *res;
}

int main(){
  int A[n][n] = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
  };
  int B[n][n] = {
    {2, 0, 0},
    {0, 2, 0},
    {0, 0, 2}
  };
  int *C = matMult(A,B);
  matprint(C,3,3);
  return 0;
}
