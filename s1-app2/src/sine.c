/* sine.c
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

#define COUNT_OF( arr) (sizeof(arr)/sizeof(0[arr]))

// Nombre de sommes à effectuer
const int precision = 16;


double pow(double b, int e){
  if (e==1){
    return b;
  } else if (e%2==0){
    return pow(b*b,e/2);
  } else {
    return b * pow(b*b, e/2);
  }
}


double sin(double input){
  double ttl = input;
  int denom = 1;
  double num;
  for (int i=3;i<(2*precision)+2;i+=2) {
    num = pow(input,i);
    denom *= i*(i-1);
    ttl += ((i-1)/2 %2 == 1) ? -num/denom : num/denom;
  }
  return ttl;
}

int main(){

  double values[] = {
    1,
    0,
    0.7853982,
    1.570796
  };
  double results[] = {
    0.8415,
    0,
    0.7071,
    1
  };
  for (int i=0;i<COUNT_OF(values);i++){
    printf("Valeur du sin: %.4f Résultat voulu: %.4f, Résultat obtenu: %.4f\n",
        values[i],
        results[i],
        sin(values[i]));
  };

  return 0;
}
