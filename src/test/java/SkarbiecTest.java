import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SkarbiecTest {
    @Test
    void testObliczOptymalnaReszte() {
        Skarbiec skarbiec = new Skarbiec();
        skarbiec.zaladujStartowe(10);

        List<Nominal> reszta = skarbiec.obliczOptymalnaReszte(3.70);
        double wartoscReszty = skarbiec.przeliczNaKwote(reszta);

        assertEquals(3.70, wartoscReszty, "Kwota reszty powinna wynosić równo 3.70 PLN");
        assertTrue(reszta.contains(Nominal.PLN_2_00), "Reszta powinna zawierać monetę 2 PLN");
        assertTrue(reszta.contains(Nominal.PLN_1_00), "Reszta powinna zawierać monetę 1 PLN");
        assertTrue(reszta.contains(Nominal.GR_50), "Reszta powinna zawierać monetę 50 GR");
        assertTrue(reszta.contains(Nominal.GR_20), "Reszta powinna zawierać monetę 20 GR");
    }
    @Test
    void testObliczWrzut() {
        Skarbiec skarbiec = new Skarbiec();

        List<Nominal> wrzucone = skarbiec.obliczWrzut(5.80);
        double wartoscWrzucona = skarbiec.przeliczNaKwote(wrzucone);

        assertEquals(5.80, wartoscWrzucona, "Rozbity wrzut powinien sumować się do 5.80 PLN");
    }
}