package be.cosci.ibm.ucllwatson;

import android.app.Activity;
import android.util.Log;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import be.cosci.ibm.ucllwatson.activity.SearchActivity;
import be.cosci.ibm.ucllwatson.adapter.IngredientRecycleViewAdapter;
import be.cosci.ibm.ucllwatson.db.item.PhotoItem;


/**
 * Created by henokv on 28/03/18.
 */
public class WatsonImplementation {

    public static void find(final PhotoItem photoItem,
                            final IngredientRecycleViewAdapter ingredientRecycleViewAdapter,
                            final SearchActivity searchActivity) throws FileNotFoundException {
        String path = photoItem.getPath();
        VisualRecognition service = new VisualRecognition(
                "2016-05-20",
                "5ef486ed2fef95bdfedd8e3c73b607df0e051053"
        );
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(new File(path))
                .threshold(0.8f)
                .addClassifierId("food")
                .build();
        service.classify(classifyOptions).enqueue(new ServiceCallback<ClassifiedImages>() {
            @Override
            public void onResponse(ClassifiedImages response) {
                String c = optimalClass(response.getImages().get(0).getClassifiers().get(0));
                photoItem.setIngredientName(c);
                ingredientRecycleViewAdapter.updateIngredient(photoItem);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        });
    }

    public static String optimalClass(ClassifierResult classifierResult) {
        String name = "Unknown";
        ClassResult cl = null;
        for (ClassResult c : classifierResult.getClasses()) {
            if (inLists(c)) {
                if (cl == null) {
                    cl = c;
                    name = c.getClassName();
                } else if (c.getScore() > cl.getScore()) {
                    cl = c;
                    name = c.getClassName();
                }
            }
        }
        return name;
    }

    public static boolean inLists(ClassResult c) {
        boolean a = false;
        String name = c.getClassName().toLowerCase();
        List<String> f = Arrays.asList(fruits);
        if (f.contains(name)) a = true;
        List<String> v = Arrays.asList(vegetables);
        if (v.contains(name)) a = true;
        List<String> o = Arrays.asList(others);
        if (o.contains(name)) a = true;
        return a;
    }

    private static final String[] fruits = {"apple", "apricot", "avocado", "banana", "bilberry", "blackberry", "blackcurrant", "blueberry", "boysenberry", "buddhas hand", "crab apples", "currant", "cherry", "cherimoya", "chico fruit", "cloudberry", "coconut", "cranberry", "cucumber", "custard apple", "damson", "date", "dragonfruit", "durian", "elderberry", "feijoa", "fig", "goji berry", "gooseberry", "grape", "raisin", "grapefruit", "guava", "honeyberry", "huckleberry", "jabuticaba", "jackfruit", "jambul", "jujube", "juniper berry", "kiwano", "kiwifruit", "kumquat", "lemon", "lime", "loquat", "longan", "avocado", "chili pepper", "corn kernel", "cucumber", "eggplant", "lychee", "mango", "mangosteen", "marionberry", "melon", "cantaloupe", "honeydew", "watermelon", "miracle fruit", "mulberry", "nectarine", "nance", "olive", "orange", "blood orange", "clementine", "mandarine", "tangerine", "papaya", "passionfruit", "peach", "pear", "persimmon", "plantain", "plum", "prune", "pineapple", "plumcot", "pomegranate", "pomelo", "purple mangosteen", "quince", "raspberry", "salmonberry", "rambutan", "redcurrant", "salal berry", "salak", "satsuma", "soursop", "star fruit", "gonzoberry", "strawberry", "tamarillo", "tamarind", "ugli fruit", "yuzu"};
    private static final String[] vegetables = {"artichoke", "aubergine", "eggplant", "amaranth", "asparagus", "legumes", "alfalfa sprouts", "azuki beans", "bean sprouts", "black beans", "black-eyed peas", "borlotti bean", "broad beans", "chickpeas", "green beans", "kidney beans", "lentils", "lima beans", "mung beans", "navy beans", "pinto beans", "runner beans", "split peas", "soy beans", "peas", "mangetout", "snap peas", "broccoflower", "broccoli", "brussels sprouts", "cabbage", "kohlrabi", "cauliflower", "celery", "endive", "fiddleheads", "frisee", "fennel", "greens", "beet greens", "bok choy", "chard", "collard greens", "kale", "mustard greens", "spinach", "anise", "basil", "caraway", "cilantro", "coriander", "chamomile", "dill", "fennel", "lavender", "lemon Grass", "marjoram", "oregano", "parsley", "rosemary", "sage", "thyme", "lettuce", "arugula", "mushrooms", "nettles", "spinach", "okra", "onions", "Chives", "Garlic", "Leek", "onion", "shallot", "scallion", "parsley", "peppers", "bell pepper", "chili pepper", "Jalapeño", "Habanero", "Paprika", "Tabasco pepper", "Cayenne pepper", "radicchio", "rhubarb", "root vegetables", "beetroot", "beet", "mangel-wurzel", "carrot", "celeriac", "daikon", "ginger", "parsnip", "rutabaga", "turnip", "radish", "wasabi", "horseradish", "white radish", "salsify", "skirret", "sweetcorn", "topinambur", "squashes", "acorn squash", "bitter melon", "butternut squash", "banana squash", "courgette", "cucumber", "delicata", "gem squash", "hubbard squash", "marrow", "patty pans", "pumpkin", "spaghetti squash", "tat soi", "tomato", "tubers", "jicama", "jerusalem artichoke", "potato", "quandong", "sunchokes", "sweet potato", "taro", "yam", "water chestnut", "watercress", "zucchini"};
    private static final String[] others = {"cheese", "butter", "milk", "eggs", "yoghurt", "cream", "syrup", "scampi", "gamba", "shrimp", "meat", "veal", "chicken", "beef"};
}