import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Testes {
    @Test
    public void tudoSubindoEPertoDaBandaDeBoll(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertTrue(tester.verificaVenda(64, 64, 75, 28, 72, 20, 10, 64));
    }

    @Test
    public void mediaMovelCaindo(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaVenda(64, 64, 75, 28, 72, 20, 10, 25));
    }

    @Test
    public void candleForcaSubida(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaVenda(64, 64, 75, 28, 72, 15, 10, 64));
    }

    @Test
    public void distanteDaBandaDeBoll(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaVenda(64, 64, 75, 82, 12, 20, 10, 64));
    }

    @Test
    public void ifrMinusculo(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaVenda(64, 64, 8, 28, 72, 20, 10, 32));
    }

    @Test
    public void toposEValesCaindo(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaVenda(32, 32, 75, 28, 72, 20, 10, 32));
    }





    @Test
    public void tudoCaindoELongeDaBandaDeBoll(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertTrue(tester.verificaCompra(32, 32, 22, 72, 28, 20, 1, 28));
    }

    @Test
    public void mediaMovelSubindo(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaCompra(32, 32, 22, 72, 28, 20, 1, 72));
    }

    @Test
    public void candleForcaQueda(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaCompra(32, 32, 22, 72, 28, 0, 1, 28));
    }

    @Test
    public void pertoDaBandaDeBoll(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaCompra(32, 32, 22, 28, 72, 20, 1, 28));
    }

    @Test
    public void ifrEnorme(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaCompra(32, 32, 88, 72, 28, 20, 1, 28));
    }

    @Test
    public void toposEValesSubindo(){
        trabalho1IA2 tester= new trabalho1IA2();
        assertFalse(tester.verificaCompra(64, 64, 75, 28, 72, 20, 1, 32));
    }

}
