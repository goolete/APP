#include <IAP15.h>
#include <sys.h>

unsigned int date[4] = {0};
unsigned int dat = 0;
unsigned char validsign = 0;
char cou = 0;

void Send_String(unsigned char *a);
void Wait_For_Char(unsigned char d);
	
void UartInit(void)		//115200bps@11.0592MHz
{
	SCON = 0x50;		//8λ����,�ɱ䲨����
	AUXR |= 0x40;		//��ʱ��1ʱ��ΪFosc,��1T
	AUXR &= 0xFE;		//����1ѡ��ʱ��1Ϊ�����ʷ�����
	TMOD &= 0x0F;		//�趨��ʱ��1Ϊ16λ�Զ���װ��ʽ
	TL1 = 0xE8;		//�趨��ʱ��ֵ
	TH1 = 0xFF;		//�趨��ʱ��ֵ
	ET1 = 0;		//��ֹ��ʱ��1�ж�
	TR1 = 1;		//������ʱ��1
	
}
//��ʼ��ESP8266
void ESP_8266_Init()
{
	Send_String("AT+CWMODE=1\r\n");
	Wait_For_Char('K');
	delayms(5);
	Send_String("AT+CIPMODE=1\r\n");
	Wait_For_Char('K');
	delayms(5);
	Send_String("AT+CIPMUX=0\r\n");
	Wait_For_Char('K');
	delayms(5);
	Send_String("AT+CWJAP=\"WiFiBase\",\"123456789\"\r\n");
	Wait_For_Char('K');
	delayms(5);
	Send_String("AT+CIPSTART=\"TCP\",\"192.168.43.1\",3358\r\n");
	Wait_For_Char('K');
	delayms(5);
	Send_String("AT+CIPSEND\r\n");
	Wait_For_Char('K');
	delayms(5);
	
	ES = 1;
	EA = 1;
}
//�����ַ���Ϣ��app�˵���Ļ��
//a �ַ���ָ��                   ;
//return
//*ע ���ַ���ĩ������\n ����ESP8266�޷�����
void Send_String(unsigned char *a)
{
	while(*a!='\0')
    {       
        SBUF = *a;
			while(!TI);
			TI = 0;
        a++;
    }  
}
void Wait_For_Char(unsigned char d)
{
	unsigned char dc;
	while(1)
	{
		while(!RI);
		RI = 0;
		dc = SBUF;
		if(dc == d)break;
			
	}
}
//�õ�ԭʼ����
//c:ԭʼ�������  unsigned char  0-3
//return ������ֵ unsigned int  0-65535
unsigned int getDate(unsigned char c)
{
	return date[c];
}

//�õ�ҡ�˰뾶���̶ȣ� 
// 
//return ҡ�˰뾶  unsigned char  0-100
unsigned char getR()
{
	return (unsigned char)(date[0]>>9);
}
//�õ�ҡ�˽Ƕ�
//
//return ҡ�˽Ƕ� unsigned char 0-359
unsigned int getRad()
{
	return date[0]&0x01ff;
	
}
//�õ���������ֵ
//c ��������� unsigned char 0-3
//return ��������ֵ unsigned char 0-100
unsigned char getProgress(unsigned char c)
{
	return (unsigned char)(date[c/2+1]>>(8-(8 * (c%2))));
}
//�õ����ص�״̬
//c ������� unsigned char 0-7
//return ״̬ unsigned char 0-1
unsigned char getK(unsigned char c)
{
	return (unsigned char)((date[3]>>(7-c))&0x1);
}
void RI_Interrupt() interrupt 4
{
	unsigned char a;
	if(RI)
	{
		RI = 0;
		a = SBUF;
	  if(a == '\n')
		{
			cou = 0;
			validsign = 1;
		}
		else if(a == ' ')
		{
			date[cou] = dat;
			dat = 0;
			cou ++;
			if(cou>=4){cou = 0;}
		}
		else if((a-48)>=0&&(a-48)<=9)
		{
			dat*=10;
			dat+=(a-48);
		}
	}
}