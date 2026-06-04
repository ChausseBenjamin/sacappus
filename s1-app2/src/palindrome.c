/* palindrome.c
 * ----------------------------------------------------------------------------
 * Auteurs:
 *    Benjamin Chausse  - chab1704
 *    Guillaume Malgorn - malg1503
 * Description:
 *     Fonction capable de déterminer si un mot est un palindrome
 *     ainsi que ses tests de validation.
 * Date: 2022-10-04
 * ----------------------------------------------------------------------------
 */

#include <stdio.h>

int palindrome(char *s){
  int len = 0;
  while (s[len]!= '\0'){
    len ++;
  }
  int j = len;
  for (int i=0; i<len/2;i++){
    j--;
    if (s[i] != s[j]){
      return 0;
    }
  }
  return 1;
}

int main(){
  char *word = "tenet";
  printf("Mot choisi: %s Résultat: %d\n",word,palindrome(word));
  word = "automobile";
  printf("Mot choisi: %s Résultat: %d\n",word,palindrome(word));
  word = "redder";
  printf("Mot choisi: %s Résultat: %d\n",word,palindrome(word));
  return 0;
}
