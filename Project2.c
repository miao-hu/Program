#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
int main()
{
	int i, j, k,n=0;
	printf("«Î ‰»Înµƒ÷µ\n");
	scanf("%d", &n);
	for (i = 1; i < n;i++)
	{
		for (j = 1; j <= i; j++)
		{
			k = i*j;
			printf("%d*%d=%2d  ", i, j, k);
		}
		printf("\n");
	}
	
	system("pause");
	return 0;
}