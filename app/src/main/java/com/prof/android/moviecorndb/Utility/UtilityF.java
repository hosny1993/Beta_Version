package com.prof.android.moviecorndb.Utility;

/**
 * Created by prof on 10/6/15.
 */
public class UtilityF {

    public static String parseRevenue (String bigNumber){

        String result = "";
        int bigNumberLength = bigNumber.length();

        for ( int i = 0; i < bigNumber.length(); i++ ){

            char value1 ;
            char value2 ;
            char value3 ;

            int index1 = ( bigNumber.length() - i - 1 ) ;
            int index2 = ( bigNumber.length() - i - 2 ) ;
            int index3 = ( bigNumber.length() - i - 3 ) ;

            if ( index1 == -1 ){
                break;
            }

            if ( index2 == -1 ){
                value1 = bigNumber.charAt( index1 );
                result = value1 + result;
                break;
                //return result;
            }

            if (index3 == -1){
                value1 = bigNumber.charAt( index1 );
                value2 = bigNumber.charAt( index2 );
                result = "" + value2 + value1 +result;
                break;
                //return result;
            }
            value1 = bigNumber.charAt( index1 );
            value2 = bigNumber.charAt( index2);
            value3 = bigNumber.charAt( index3);

            if (index3 == 0)
                result = "" + value3 + value2 + value1 + result;
            else
                result = "," + value3 + value2 + value1 + result;

            i = i + 2;
        }

        return result;
    }
}
