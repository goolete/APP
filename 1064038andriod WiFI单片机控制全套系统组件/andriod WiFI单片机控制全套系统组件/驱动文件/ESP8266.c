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
	SCON = 0x50;		//8位数据,可变波特率
	AUXR |= 0x40;		//定时器1时钟为Fosc,即1T
	AUXR &= 0xFE;		//串口1选择定时器1为波特率发生器
	TMOD &= 0x0F;		//设定定时器1为16位自动重装方式
	TL1 = 0xE8;		//设定定时初值
	TH1 = 0xFF;		//设定定时初值
	ET1 = 0;		//禁止定时器1中断
	TR1 = 1;		//启动定时器1
	
}
//初始化ESP8266
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
//发送字符信息到app端的屏幕上
//a 字符串指针                   ;
//return
//*注 ：字符串末必须有\n 否则ESP8266无法发送
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
//得到原始数据
//c:原始数据序号  unsigned char  0-3
//return 该数据值 unsigned int  0-65535
unsigned int getDate(unsigned char c)
{
	return date[c];
}

//得到摇杆半径（程度） 
// 
//return 摇杆半径  unsigned char  0-100
unsigned char getR()
{
	return (unsigned char)(date[0]>>9);
}
//得到摇杆角度
//
//return 摇杆角度 unsigned char 0-359
unsigned int getRad()
{
	return date[0]&0x01ff;
	
}
//得到进度条的值
//c 进度条序号 unsigned char 0-3
//return 进度条的值 unsigned char 0-100
unsigned char getProgress(unsigned char c)
{
	return (unsigned char)(date[c/2+1]>>(8-(8 * (c%2))));
}
//得到开关的状态
//c 开关序号 unsigned char 0-7
//return 状态 unsigned char 0-1
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