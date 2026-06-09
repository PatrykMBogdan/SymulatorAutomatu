/**
 * Typ wyliczeniowy definiujący możliwe wyniki próby zakupu produktu.
 * Pozwala na kategoryzację błędów oraz udanych operacji do późniejszych statystyk.
 *
 * @author Szymon Łakomy, Patryk Bogdan
 */
public enum StatusTransakcji {
    UDANA,
    ZA_MALO_GOTOWKI,
    BRAK_PRODUKTU,
    NIE_MA_JAK_WYDAC
}
