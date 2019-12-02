import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class trabalho1IA2 {


    private static double mediaMovelSimples(double[] valores, int periodo){
        double media= 0;
        for(int i= valores.length-(periodo+1); i<valores.length; i++){
            media+= valores[i];
        }
        return media/(double)(periodo);
    }

    private static double desvioPadrao(double[] valores, double media){
        double desvio= 0;
        for(int i= valores.length-(21); i<valores.length; i++){
            desvio+= pow(valores[i] - media, 2);
        }
        return sqrt(desvio/20);
    }

    private static double mediaMovelExp (double[] valores){
        double media= mediaMovelSimples(valores, 14);
        double k= 2.0/10.0;
        double tendencia= 0;
        double anterior= 1;
        for(int i= 1; i<valores.length; i++){
            anterior= media;
            media+= (valores[i]-media)*k;
            tendencia+= (media-anterior)*k;
        }
        return 50 + 500*((anterior+tendencia)/anterior - 1);
    }


    private static double forcaRelativa (double[] valores){
        double ganhos= 0;
        double perdas= 0;
        double anterior= valores[0];
        for(int i= 1; i<15; i++){
            if(valores[i] > anterior) {
                ganhos += valores[i] - anterior;
            }else {
                perdas += anterior - valores[i];
            }
            anterior= valores[i];
        }
        ganhos/= 14;
        perdas/= 14;
        for(int i= 15; i<valores.length; i++){
            if(valores[i] > anterior) {
                ganhos= (ganhos*13 + valores[i]-anterior)/14;
            }else {
                perdas= (perdas*13 + (anterior-valores[i]))/14;
            }
            anterior= valores[i];
        }
        double fr= ganhos/perdas;
        return 100 - 100/(1 + fr);
    }

    private static double accdist(double[] min, double[] max, double[] valores, double[] volume){
        double acumulador= 0;
        for(int i= valores.length-30; i<valores.length; i++){
            double temp= (valores[i]- min[i]) - (max[i] - valores[i]);
            temp/= (max[i] - min[i]);
            //temp*= volume[i];
            acumulador+= temp;
        }
        return acumulador;
    }

    private static double topos(double[] valores){
        List<Double> diferencas= new ArrayList<>();
        double anterior= -1;
        if(valores[0] > valores[1]){
            anterior= valores[0];
        }
        for(int i= 1; i<valores.length - 1; i++){
            if(valores[i] < valores[i+1])
                continue;
            if(valores[i] > valores[i-1]){
                if(anterior > 0){
                    diferencas.add(valores[i] - anterior);
                }
                anterior= valores[i];
            }
        }
        double retorno= (anterior+mediaMovelDiferencas(diferencas))/anterior;
        if(retorno>2)
            retorno= 2;
        if(retorno<0.5)
            retorno= 0.5;
        return 100*1.5*(retorno-0.5);
    }

    private static double vales(double[] valores){
        List<Double> diferencas= new ArrayList<>();
        double anterior= 0;
        if(valores[0] < valores[1]){
            anterior= valores[0];
        }
        for(int i= 1; i<valores.length - 1; i++){
            if(valores[i] > valores[i+1])
                continue;
            if(valores[i] < valores[i-1]){
                if(anterior > 0){
                    diferencas.add(valores[i] - anterior);
                }
                anterior= valores[i];
            }
        }
        double retorno= (anterior+mediaMovelDiferencas(diferencas))/anterior;
        if(retorno>2)
            retorno= 2;
        if(retorno<0.5)
            retorno= 0.5;
        return 100*1.5*(retorno-0.5);
    }

    private static double mediaMovelDiferencas(List<Double> diferencas) {
        double mediaSimples= 0;
        int periodo= 14;
        if(diferencas.size() < 14){
            if(diferencas.size() < 9)
                periodo= 0;
            else
                periodo= 9;
        }
        for(int i= diferencas.size() - periodo; i<diferencas.size(); i++){
            mediaSimples+= diferencas.get(i);
        }
        return mediaSimples/= 9;
    }

    private static double tipoCandle(double[] abertura, double[] fechamento, double[] min, double[] max){
        double aberturaAtual= abertura[abertura.length-1];
        double fechamentoAtual= fechamento[fechamento.length-1];
        double minAtual= min[min.length-1];
        double maxAtual= max[max.length-1];
        if(fechamentoAtual > aberturaAtual){                                    //alta
            if(maxAtual-fechamentoAtual < 0.03 && aberturaAtual-minAtual > 0.1){
                return 10.0;                                            //Reversão
            }
            if(aberturaAtual-minAtual < 0.02 && maxAtual-fechamentoAtual<0.03){
                return 15.0;                                            //Forca
            }
        }else{                                                                  //baixa
            if(fechamentoAtual-minAtual < 0.03 && maxAtual-aberturaAtual > 0.1){
                return 0.0;                                             //Reversão
            }
            if(maxAtual-aberturaAtual<0.02 && fechamentoAtual-minAtual<0.03){
                return 5.0;                                             //Forca
            }
        }
        return 20.0;                                                    //Nenhum
    }

    private static double tendencia(double[] abertura, double[] fechamento, double[] min, double[] max){
        double[] oscilacaoMedia= new double[fechamento.length/10];
        boolean congestao= false;
        for(int i= 0; i<fechamento.length-1; i+= 10){
            if(i+10 >= fechamento.length){
                break;
            }
            double menor= Double.POSITIVE_INFINITY, maior= Double.NEGATIVE_INFINITY;
            for(int j= i; j<i+10; j++){
                if(min[j] < menor)
                    menor= min[j];
                if(maior < max[j])
                    maior= max[j];
            }
            oscilacaoMedia[i/10]= maior-menor;
        }
        for(int oscilacao= 0; oscilacao<oscilacaoMedia.length-1; oscilacao++){
            if (oscilacaoMedia[oscilacao] > 2 * oscilacaoMedia[oscilacaoMedia.length - 1]) {
                congestao = true;
                break;
            }
        }
        if(congestao){
            return 5.0;
        }else{
            if(mediaMovelExp(fechamento) > 0){
                return 10.0;
            }else{
                return 0.0;
            }
        }
    }

    public boolean verificaCompra(double topos, double vales, double ifr, double distBollAlt, double distBollBaix, double tipoCandle, double movimento, double mediasMoveis){
        FunctionBlock fb= aplicarRegrasFuzzy(topos, vales, ifr, distBollAlt, distBollBaix, tipoCandle, movimento, mediasMoveis);
        //System.out.println(fb.getVariable("comprar").getValue());
        return fb.getVariable("comprar").getValue() >= 5;
    }

    public boolean verificaVenda(double topos, double vales, double ifr, double distBollAlt, double distBollBaix, double tipoCandle, double movimento, double mediasMoveis){
        FunctionBlock fb= aplicarRegrasFuzzy(topos, vales, ifr, distBollAlt, distBollBaix, tipoCandle, movimento, mediasMoveis);
        //System.out.println(fb.getVariable("vender").getValue());
        return fb.getVariable("vender").getValue() >= 5;
    }

    private FunctionBlock aplicarRegrasFuzzy(double topos, double vales, double ifr, double distBollAlt, double distBollBaix, double tipoCandle, double movimento, double mediasMoveis) {
        FIS fis= FIS.load(Paths.get("").toAbsolutePath().toString()+"\\src\\main\\resources\\SM.fcl", true);
        FunctionBlock fb = fis.getFunctionBlock(null);
        fb.setVariable("picos", topos);
        fb.setVariable("vales", vales);
        fb.setVariable("ifr", ifr);
        fb.setVariable("distbollbaix", distBollBaix);
        fb.setVariable("distbollalt", distBollAlt);
        fb.setVariable("tipocandle", tipoCandle);
        fb.setVariable("movimento", movimento);
        fb.setVariable("mediasmovs", mediasMoveis);
        fb.getVariable("comprar").setDefaultValue(0);
        fb.getVariable("vender").setDefaultValue(0);
        fb.evaluate();
        return fb;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("PBR.csv"));
        String row;
        double[] tempAbert= new double[350];
        double[] tempMin= new double[350];
        double[] tempMax= new double[350];
        double[] tempFech= new double[350];
        double[] tempVol= new double[350];
        int numDias= -1;
        while ((row = csvReader.readLine()) != null) {
            numDias++;
            if(numDias == 0)
                continue;
            String[] data = row.split(",");
            tempAbert[numDias-1]= Double.parseDouble(data[1]);
            tempMax[numDias-1]= Double.parseDouble(data[2]);
            tempMin[numDias-1]= Double.parseDouble(data[3]);
            tempFech[numDias-1]= Double.parseDouble(data[5]);
            tempVol[numDias-1]= Double.parseDouble(data[6]);
        }
        csvReader.close();
        double[] abertura= new double[numDias];
        double[] fechamento= new double[numDias];
        double[] min= new double[numDias];
        double[] max= new double[numDias];
        double[] volume= new double[numDias];

        for(int i= 0; i<numDias; i++){
            abertura[i]= tempAbert[i];
            fechamento[i]= tempFech[i];
            min[i]= tempMin[i];
            max[i]= tempMax[i];
            volume[i]= tempVol[i];
        }
        tempAbert= tempFech= tempMax= tempMin= tempVol= null;

        double mediaBoll= mediaMovelSimples(fechamento, 20);
        double desvioBoll= desvioPadrao(fechamento, mediaBoll);
        double tetoBoll= mediaBoll + 2*desvioBoll;
        double chaoBoll= mediaBoll - 2*desvioBoll;

        double topo= topos(fechamento);
        double vale= vales(fechamento);
        double ifr= forcaRelativa(fechamento);
        double tipo= tipoCandle(abertura, fechamento, min, max);
        double movimento= tendencia(abertura, fechamento, min, max);
        //double acdis= accdist(min, max, fechamento, volume);
        double mediaExp= mediaMovelExp(fechamento);
        double distTetoBoll= 100*(tetoBoll-fechamento[fechamento.length-1])/(tetoBoll-chaoBoll);
        double distChaoBoll= 100*(fechamento[fechamento.length-1]-chaoBoll)/(tetoBoll-chaoBoll);
        /*
        System.out.println(topo);
        System.out.println(vale);
        System.out.println(ifr);
        System.out.println(tipo);
        System.out.println(movimento);
        //System.out.println(acdis);
        System.out.println(mediaExp);
        System.out.println(distTetoBoll);
        System.out.println(distChaoBoll);
        */
        FIS fis= FIS.load(Paths.get("").toAbsolutePath().toString()+"\\src\\main\\resources\\SM.fcl", true);
        FunctionBlock fb = fis.getFunctionBlock(null);
        JFuzzyChart.get().chart(fb);
        //JFuzzyChart.get().chart(fb);
        fb.setVariable("picos", topo);
        fb.setVariable("vales", vale);
        fb.setVariable("ifr", ifr);
        fb.setVariable("distbollbaix", distChaoBoll);
        fb.setVariable("distbollalt", distTetoBoll);
        fb.setVariable("tipocandle", tipo);
        fb.setVariable("movimento", movimento);
        //fb.setVariable("accdist", acdis);
        fb.setVariable("mediasmovs", mediaExp);
        fb.getVariable("comprar").setDefaultValue(0);
        fb.getVariable("vender").setDefaultValue(0);
        fb.evaluate();

        //System.out.println(fb.getVariable("picos").getMembership("subindo"));
        //System.out.println(fb.getVariable("vales").getMembership("subindo"));
        //System.out.println(fb.getVariable("ifr").getMembership("alto"));
        //System.out.println(fb.getVariable("distbollalt").getMembership("subindo"));
        if(fb.getVariable("comprar").getValue() > 0) {
            System.out.println("É momento para se comprar ações, com força de " + fb.getVariable("comprar").getValue() + "%");
        }else{
            System.out.println("Não é momento para se comprar ações.");
        }
        if(fb.getVariable("vender").getValue() > 0) {
            System.out.println("É momento para se vender ações, com força de " + fb.getVariable("vender").getValue() + "%");
        }else{
            System.out.println("Não é momento para se vender ações.");
        }
        //JFuzzyChart.get().chart(fb.getVariable("comprar"),fb.getVariable("comprar").getDefuzzifier(), true);
    }

}
