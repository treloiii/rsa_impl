import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args){

        //---------------------------
        BigInteger pA=generatePseudoSimple();
        BigInteger qA=generatePseudoSimple();

        BigInteger nA =pA.multiply(qA);
        BigInteger eulerFuncA=eulerFunc(pA,qA);
        BigInteger e=new BigInteger("65537");
        BigInteger dA=e.modPow(BigInteger.valueOf(-1),eulerFuncA);

        BigInteger[] publicKeyA=new BigInteger[]{e,nA};
        BigInteger[] privateKeyA=new BigInteger[]{dA,nA};
        //--------------------------------- действия алисы: алиса генерирует открытый и закрытый ключ
        //пересылает открытый ключ бобу

        String textFromBob="Hello Alice";
        BigInteger encrypted=encryptMessage(textFromBob,publicKeyA);//Боб берет открытый ключ и шифрует сообщение
        //боб пересылает зашифрованное сообщение алисе---->
        String decrypted=decryptMessage(encrypted,privateKeyA);//Алиса получает зашифрованное сообщение боба и расшифровывает его своим закрытым ключом
        System.out.println(decrypted);//Передача окончена


        //---------------------------
        BigInteger pB=generatePseudoSimple();
        BigInteger qB=generatePseudoSimple();

        BigInteger nB =pB.multiply(qB);
        BigInteger eulerFuncB=eulerFunc(pB,qB);
        BigInteger e1=new BigInteger("257");
        BigInteger dB=e1.modPow(BigInteger.valueOf(-1),eulerFuncB);

        BigInteger[] publicKeyB=new BigInteger[]{e1,nB};
        BigInteger[] privateKeyB=new BigInteger[]{dB,nB};
        //--------------------------------- действия боба: боб генерирует открытый и закрытый ключ
        //пересылает открытый ключ алисе
        String textFromAlice="Hello Bob";

        BigInteger encrypted1=encryptMessage(textFromAlice,publicKeyB);//Алиса берет открытый ключ и шифрует сообщение
        //боб пересылает зашифрованное сообщение алисе---->
        String decrypted1=decryptMessage(encrypted1,privateKeyB);//Боб получает зашифрованное сообщение алисы и расшифровывает его своим закрытым ключом
        System.out.println(decrypted1);//Передача окончена


        String bob1="how are you";
        BigInteger encrypted2=encryptMessage(bob1,publicKeyA);//отправляет алисе

        String fromBob=decryptMessage(encrypted2,privateKeyA);
        System.out.println(fromBob);
    }


    public static String decryptMessage(BigInteger encryptedMessage,BigInteger[] privateKey){
        BigInteger m=encryptedMessage.modPow(privateKey[0],privateKey[1]);
        return new String(m.toByteArray());
    }
    public static BigInteger encryptMessage(String message,BigInteger[] publicKey){
        BigInteger m=new BigInteger(message.getBytes());
        return m.modPow(publicKey[0],publicKey[1]);
    }
    public static BigInteger eulerFunc(BigInteger p,BigInteger q){
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }
    public static BigInteger generatePseudoSimple(){
        Random rd = new Random();
        byte[] arr = new byte[128];
        BigInteger c=new BigInteger(arr);
        rd.nextBytes(arr);
        while(!checkSimple(c)) {
            rd.nextBytes(arr);
            c=new BigInteger(arr);
            String c1=c.toString();
            if(c1.charAt(0)=='-') {
                c1 = c1.substring(1);
                c=new BigInteger(c1);
            }
        }
        return c;
    }
    public static BigInteger rnd(String min, String max) {
        BigInteger maxLimit = new BigInteger(max);
        BigInteger minLimit = new BigInteger(min);
//        System.out.println(minLimit.toString()+" MIN");
//        System.out.println(maxLimit.toString()+" MAX");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);
        return res;
    }
    public static boolean compareIntBigInt(int a,BigInteger b){
        return BigInteger.valueOf(a).compareTo(b)==0;
    }
    public static int modIntBigInt(int a, BigInteger b){
        return b.mod(BigInteger.valueOf(a)).intValue();
    }
    public static boolean checkSimple(BigInteger n){

        if(compareIntBigInt(2,n))
            return true;
        else if (compareIntBigInt(3,n))
            return true;
        else if(modIntBigInt(3,n)==0||modIntBigInt(2,n)==0||modIntBigInt(5,n)==0||modIntBigInt(7,n)==0||modIntBigInt(11,n)==0)
            return false;
        long k=n.bitCount();
        //System.out.println(k);
        int i=0;
        BigInteger t=n.subtract(BigInteger.ONE);
        while(modIntBigInt(2,n)==0){
            t=t.divide(BigInteger.valueOf(2));
            i++;
        }
        for(;k>0;k--){

            BigInteger a = rnd("2",n.toString());
            BigInteger x=a.modPow(t,n);
            if(x.compareTo(BigInteger.ONE)==0||x.compareTo(n.subtract(BigInteger.valueOf(1)))==0) {
                continue;
            }
            for(int r1=1;r1<i;r1++){
                x=x.modPow(BigInteger.valueOf(2),n);
                if(x.compareTo(BigInteger.ONE)==0)
                    return false;
                else if(x.compareTo(n.subtract(BigInteger.ONE))==0) {
                    break;
                }
            }
            if(x.compareTo(n.subtract(BigInteger.ONE))!=0)
                return false;
        }
        return true;
    }
}
