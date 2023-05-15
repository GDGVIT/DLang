#include <stdio.h>

const int y = 4;


struct asd {
	int me;
	char you;
	
	
	
	

};
int rrrr(struct asd *this) {
		printf("%d", this->me); //should compile
	}

char r2r2(struct asd *this) {
		printf("%c", this->you); //should also compile
	}


int main() {
	struct asd r;
	rrrr(&r);
	return 0;
}