/* matAdd.c
 * ----------------------------------------------------------------------------
 * Auteurs:
 *    Benjamin Chausse  - chab1704
 *    Guillaume Malgorn - malg1503
 * Description:
 *     Fonction trouvant le sinus d'une valeur donnée
 *     ainsi que ses tests de validation.
 * Date: 2022-10-04
 * ----------------------------------------------------------------------------
 */

#include <stdio.h>

#define m 3
#define n 2

void matprint(int *arr, int w, int h){
  int i, j;
  for (i = 0; i < w; i++) {
    for (j = 0; j < h; j++) {
      printf("%3d ", *((arr+i*h) + j));
    }
    printf("\n");
  }
}

int *matAdd(int *ma, int *mb){
  int w = n;
  int h = m;
  int res[h][w];
  for (int i=0;i<w;i++){
    for (int j=0;j<h;j++){
      res[i][j] = *(ma+(i*h)+j) + *(mb+(i*h)+j);
    }
  }
  return *res;
}

int main(){

  int mtrxA[m][n] = {
    { 1, 2 },
    { 3, 4 },
    { 5, 6 }
  };

  int mtrxB[m][n] = {
    { 6, 5 },
    { 4, 3 },
    { 2, 1 }
  };

  int *mtrxC = matAdd(mtrxA,mtrxB);

  printf("Matrice A:\n");
  matprint(mtrxA,m,n);
  printf("Matrice B:\n");
  matprint(mtrxB,m,n);
  printf("Résultat de la somme:\n");
  matprint(mtrxC,m,n);
  return 0;
}
