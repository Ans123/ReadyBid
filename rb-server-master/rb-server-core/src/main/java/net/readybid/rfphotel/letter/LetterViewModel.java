package net.readybid.rfphotel.letter;


import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 1/8/2017.
 *
 */
public class LetterViewModel implements ViewModel<String> {

    public static ViewModelFactory<String, LetterViewModel> FACTORY = LetterViewModel::new;

    public String letter;

    public LetterViewModel(String letter) {
        this.letter = letter;
    }
}