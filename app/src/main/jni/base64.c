#include "base64.h"
const char _Base[] = {
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=" };

static union {
	struct {
		unsigned long a :6;
		unsigned long b :6;
		unsigned long c :6;
		unsigned long d :6;
	} Sdata;
	unsigned char c[3];
} Udata;

char * Decbase64(char * orgdata, unsigned long orglen) {
	char *p, *ret;
	int len;
	char ch[4] = { 0 };
	char *pos[4];
	int offset[4];
	if (orgdata == NULL || orglen == 0) {
		return NULL;
	}
	len = orglen * 3 / 4;
	if ((ret = (char *) malloc(len + 1)) == NULL) {
		return NULL;
	}
	p = orgdata;
	len = orglen;
	int j = 0;

	while (len > 0) {
		int i = 0;
		while (i < 4) {
			if (len > 0) {
				ch[i] = *p;
				p++;
				len--;
				if ((pos[i] = (char *) strchr(_Base, ch[i])) == NULL) {
					return NULL;
				}
				offset[i] = pos[i] - _Base;

			}
			i++;
		}
		if (ch[0] == '=' || ch[1] == '=' || (ch[2] == '=' && ch[3] != '=')) {
			return NULL;
		}
		ret[j++] = (unsigned char) (offset[0] << 2 | offset[1] >> 4);
		ret[j++] = offset[2] == 64 ? '\0' : (unsigned char) (offset[1] << 4 | offset[2] >> 2);
		ret[j++] = offset[3] == 64 ? '\0' : (unsigned char) ((offset[2] << 6 & 0xc0) | offset[3]);
	}
	ret[j] = '\0';
	return ret;
}
