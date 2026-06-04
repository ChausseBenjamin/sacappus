/* findChar.c
 * ----------------------------------------------------------------------------
 * Auteurs:
 *    Benjamin Chausse  - chab1704
 *    Guillaume Malgorn - malg1503
 * Description:
 *     Fonction trouvant la position d'un caractère
 *     voulu dans une chaine de caractère (string)
 *     donnée ainsi que ses tests de validation.
 * Date: 2022-10-04
 * ----------------------------------------------------------------------------
 */

#include <stdio.h>


int findChar(char *mot,char target){
  int len = 0;
  while (mot[len]!= '\0'){
    len ++;
  }
  for (int i=0;i<len;i++){
    if (mot[i] == target){
      return i;
    }
  }
  return -1;
}

int main(){

  char *word = "anticonstitutionellement";
  char lettre = 'n';
  printf("Résultat: %2d, Lettre: %c, Mot: %s\n",
      findChar(word,lettre),
      lettre,
      word);

  word = "bonjour";
  lettre = 'e';
  printf("Résultat: %2d, Lettre: %c, Mot: %s\n",
      findChar(word,lettre),
      lettre,
      word);

  word = "bonjour";
  lettre = 'r';
  printf("Résultat: %2d, Lettre: %c, Mot: %s\n",
      findChar(word,lettre),
      lettre,
      word);

  word = "allocommentcava";
  lettre = 'a';
  printf("Résultat: %2d, Lettre: %c, Mot: %s\n",
      findChar(word,lettre),
      lettre,
      word);
      return 0;
}




