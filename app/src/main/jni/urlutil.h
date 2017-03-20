#include <stdio.h>  
#include <string.h>  

#define BURSIZE 1024  

int hex2dec(char c) ;

char dec2hex(short int c) ;

/* 
 * 编码一个url
 */
void urlencode(char *url);

/* 
 * 解码url
 */
void urldecode(char *url) ;
