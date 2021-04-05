package com.nexm.goseller.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexm.goseller.GO_SELLER_APPLICATION;
import com.nexm.goseller.R;
import com.nexm.goseller.models.Delivery;
import com.nexm.goseller.models.ProductDetails;
import com.nexm.goseller.models.ProductListing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String productID,unit;
    private final String[] stepTitles = {"","Department","Photos","Product Info","Delivery info"};
    private ArrayList<String> PRICES ;
    private int counter = 1,progress = 25,FINAL_STEP =4;
    private TextView stepName,stepTitle,previousButton,pricesErrorView;
    private ProgressBar stepProgress,progressBar;
    private LinearLayout step1Layout;
    private ScrollView step2Layout,step3Layout,step4Layout;
    private EditText one,two,three,four,five,six,namefield,pricefield,offerfield,
            descriptionfield,quantityfield,dtime,dreturnPolicy,dminOrder,dcharges;
    private Switch switchCOD,switchReturn;
    private DatabaseReference reference;
    private Spinner spinner1,spinner2,spinner3;
    private String PricesString = "" ,selectedDepartment, category , subCategory;
    private ArrayList<String> departments,categories,subCategories;
    private ProductListing productListing;
    private ProductDetails productDetails;
    private Delivery delivery;
    private boolean NEW ;

    private OnFragmentInteractionListener mListener;

    public AddNewProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productID Parameter 1.
     * @return A new instance of fragment AddNewProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewProductFragment newInstance(String productID) {
        AddNewProductFragment fragment = new AddNewProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, productID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productID = getArguments().getString(ARG_PARAM1);
        }
        NEW = productID.matches("New");
        reference = FirebaseDatabase.getInstance().getReference();
        productDetails = new ProductDetails();
        delivery = new Delivery();
        productListing = new ProductListing();
        PRICES = new ArrayList<>();
        if(NEW){
            productListing.setSellerID(GO_SELLER_APPLICATION.sellerID);
            productListing.setSellerName(GO_SELLER_APPLICATION.sellerName);
            Date date = new Date();
            long today = date.getTime();
            productListing.setDateOfListing(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,30);
            date = calendar.getTime();
            long validity = date.getTime();
            productListing.setValidity(validity);
            PRICES = new ArrayList<>();
        }else{

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        if(GO_SELLER_APPLICATION.productsLimit == GO_SELLER_APPLICATION.noOfProducts && NEW){
            alert("You have exhausted your account products limit" +
                    "If you want to upgrade account , go to Profile section");

        }
        final TextView nextButton = view.findViewById(R.id.newP_nextButton);
        stepName = view.findViewById(R.id.newP_stepCounter);
        stepTitle = view.findViewById(R.id.newP_stepTitle);
        stepProgress = view.findViewById(R.id.newP_stepProgressBar);
        step1Layout = view.findViewById(R.id.newP_step1Layout);
        step2Layout = view.findViewById(R.id.newP_step2Layout);
        step3Layout = view.findViewById(R.id.newP_step3Layout);
        step4Layout = view.findViewById(R.id.newP_step4Layout);
        previousButton = view.findViewById(R.id.newP_previousButton);

        toggleLayouts();

        final TextView setPrices = view.findViewById(R.id.newProduct_setpricebutton);
        if(!NEW)setPrices.setVisibility(View.GONE);
        setPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPricesDialog();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(counter < FINAL_STEP){
                        if(validData()){
                            counter++;
                            stepProgress.setProgress(progress*counter);
                            toggleLayouts();
                        }
                    }else{
                        if(validData())
                            finalalert();
                    }

            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter > 1){
                    counter--;
                    toggleLayouts();
                }
            }
        });
        one = view.findViewById(R.id.newP_oneField);
        two = view.findViewById(R.id.newP_twoField);
        three = view.findViewById(R.id.newP_threeField);
        four = view.findViewById(R.id.newP_fourField);
        five = view.findViewById(R.id.newP_fiveField);
        six = view.findViewById(R.id.newP_sixField);
        namefield = view.findViewById(R.id.newP_nameField);
        pricefield = view.findViewById(R.id.newP_priceField);
        offerfield = view.findViewById(R.id.newP_offerPriceField);
        descriptionfield = view.findViewById(R.id.newP_descriptionField);
        quantityfield = view.findViewById(R.id.newP_quantityField);
        dtime = view.findViewById(R.id.newP_deliveryTimeField);
        dminOrder = view.findViewById(R.id.newP_minOrderField);
        dcharges = view.findViewById(R.id.newP_deliveryChargesField);
        dreturnPolicy = view.findViewById(R.id.newP_returnPolicyField);
        switchCOD = view.findViewById(R.id.newP_codSwitch);
        switchReturn = view.findViewById(R.id.newP_returnSwitch1);
        spinner1 = view.findViewById(R.id.newP_spinner1);
        spinner2 = view.findViewById(R.id.newP_spinner2);
        spinner3 = view.findViewById(R.id.newP_spinner3);
        pricesErrorView = view.findViewById(R.id.addnewP_pricesError);
        pricesErrorView.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.newP_progressBar);
        departments = new ArrayList<>();
        categories = new ArrayList<>();
        subCategories = new ArrayList<>();
        if(NEW)setUpSpinner1();
        if(!NEW)setProductDetails();
        return view;
    }

    private void setProductDetails() {
        reference.child("ProductListings").child(productID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            productListing = dataSnapshot.getValue(ProductListing.class);
                            selectedDepartment = productListing.getDepartment();
                            String[] deptcatsub = productListing.getDeptCatSubCat().split(",");
                            category = deptcatsub[1];
                            subCategory = deptcatsub[2];
                            namefield.setText(productListing.getProductName());
                           // quantityfield.setText(String.valueOf(productListing.getqAvailable()));
                            setUpSpinner1();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alert("Something went wrong . Please try after some time.");
                    }
                });
        reference.child("ProductDetails").child(productID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            productDetails = dataSnapshot.getValue(ProductDetails.class);
                            one.setText(productDetails.getOne());
                            two.setText(productDetails.getTwo());
                            three.setText(productDetails.getThree());
                            four.setText(productDetails.getFour());
                            five.setText(productDetails.getFive());
                            six.setText(productDetails.getSix());
                            descriptionfield.setText(productDetails.getDiscription());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alert("Something went wrong . Please try after some time.");
                    }
                });
        reference.child("Delivery").child(productID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            delivery = dataSnapshot.getValue(Delivery.class);
                            dtime.setText(delivery.getTime());
                            dminOrder.setText(String.valueOf(delivery.getMinOrder()));
                            dcharges.setText(String.valueOf(delivery.getCharges()));
                            dreturnPolicy.setText(delivery.getReturnPolicy());
                            switchCOD.setChecked(delivery.getCod());
                            switchReturn.setChecked(delivery.getReturnAvailable());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alert("Something went wrong . Please try after some time.");
                    }
                });


    }

    private void showPricesDialog() {
        final Dialog dialog;

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.weight_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // dialog.setCanceledOnTouchOutside(true);

        final Chip displayChip = dialog.findViewById(R.id.weights_display_chip);
        final ChipGroup optionalChipGroup = dialog.findViewById(R.id.weights_chipGroup);
        final ToggleButton displayPriceButton = dialog.findViewById(R.id.weights_display_price);
        final ToggleButton optionalPricesButton = dialog.findViewById(R.id.weights_optional);
        final TextView done = dialog.findViewById(R.id.weights_done);


        final EditText pricefield = dialog.findViewById(R.id.weights_offer_price);
        final EditText mrpfield = dialog.findViewById(R.id.weights_mrp);
        final EditText quantityfield = dialog.findViewById(R.id.weights_quantity);
        final EditText stockfield = dialog.findViewById(R.id.weights_stock);
        final Spinner spinner = dialog.findViewById(R.id.weights_spinner);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator iterator = PRICES.iterator();
                while(iterator.hasNext()){
                    String s = iterator.next().toString();
                    if(s.matches("removed")){
                        iterator.remove();
                    }else {
                        if(PricesString.isEmpty()){
                            PricesString=s;
                        }else{
                            PricesString=PricesString.concat(",").concat(s);
                        }
                    }
                }
                //PRICES.toString();

                dialog.dismiss();
            }
        });

       displayPriceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    displayPriceButton.setBackgroundResource(R.drawable.button);
                    optionalPricesButton.setChecked(false);
                    displayPriceButton.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.white));
                }else {
                    displayPriceButton.setBackgroundResource(R.drawable.button_outline);
                    displayPriceButton.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.darker_gray));
                }
           }
       });
        optionalPricesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    optionalPricesButton.setBackgroundResource(R.drawable.button);
                    optionalPricesButton.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.white));
                    displayPriceButton.setChecked(false);
                }else {
                    optionalPricesButton.setBackgroundResource(R.drawable.button_outline);
                    optionalPricesButton.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.darker_gray));
                }
            }
        });

        final ArrayList<String> units = new ArrayList<>();
        switch (selectedDepartment){
            case "किराना":
            case "दूध-भाजीपाला-फळे":
                units.clear();
                units.add("gm");units.add("kg");units.add("ml");units.add("L");units.add("piece");
                break;
            case "जेवण-नाश्ता-टिकावू पदा्रथ":
                units.clear();units.add("plate");units.add("piece");
                units.add("gm");units.add("kg");units.add("ml");units.add("L");
                break;
            case "ट्रांसपोर्ट":
                units.clear();units.add("km");units.add("yavatmal trip");units.add("local trip");
                break;
            case "जुना बाजार":
            case "प्रॉपट्री":
                units.clear();units.add("piece");units.add("per month");units.add("sq-feet");
                units.add("acre");
                break;

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item
                                                        ,units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    unit = units.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                unit = units.get(0);
            }
        });

        final TextView add = dialog.findViewById(R.id.weights_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q = quantityfield.getText().toString();
                String m = mrpfield.getText().toString();
                String p = pricefield.getText().toString();
                String s = stockfield.getText().toString();
                if(TextUtils.isEmpty(q)) {
                    quantityfield.setError("Required");return;
                }else{
                    quantityfield.setError(null);
                }
                if(TextUtils.isEmpty(m)) {
                    mrpfield.setError("Required");return;
                }else{
                    mrpfield.setError(null);
                }
                if(TextUtils.isEmpty(p)) {
                    pricefield.setError("Required");return;
                }else{
                    pricefield.setError(null);
                }
                if(TextUtils.isEmpty(s)) {
                    stockfield.setError("Required");return;
                }else{
                    stockfield.setError(null);
                }
                if(displayPriceButton.isChecked()){
                    productListing.setPrice(unit+"-"+q+"-"+m+"-"+p+"-"+s);
                    //productListing.setDiscount(Integer.parseInt(p));
                    productListing.setqAvailable(Integer.parseInt(s));
                    PRICES.add(0,unit+"-"+q+"-"+m+"-"+p+"-"+s);
                    displayChip.setVisibility(View.VISIBLE);
                    displayChip.setText("₹. "+p+" / "+q+unit);
                    displayPriceButton.setChecked(false);
                    optionalPricesButton.setChecked(true);
                }else{
                    int index = PRICES.size();
                    PRICES.add(unit+"-"+q+"-"+m+"-"+p+"-"+s);
                    Chip chip = new Chip(getActivity());
                    chip.setCheckable(false);
                    chip.setCloseIconVisible(true);
                   // chip.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.outline));
                    chip.setText("₹. "+p+" / "+q+unit);
                    chip.setId(index);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            optionalChipGroup.removeView(view);
                            PRICES.set(view.getId(),"removed");
                        }
                    });
                    optionalChipGroup.addView(chip);
                }
                quantityfield.setText("");
                mrpfield.setText("");
                pricefield.setText("");
                stockfield.setText("");

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void finalalert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Review and Submit")
                .setMessage("It is strongly advised to check all details before submitting. " +
                        "You can use previous and next buttons to check all steps.Once " +
                        "satisfied , click on Final Submit")
                .setPositiveButton("Review", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
        .setNegativeButton("Final Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveToFirebase();
            }
        });
        builder.show();
    }

    private void saveToFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        if(NEW){
            DatabaseReference pref = reference.child("ProductListings").push();
            productID = pref.getKey();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/ProductListings/"+productID,productListing);
        childUpdates.put("/ProductDetails/"+productID,productDetails);
        if(selectedDepartment.equals("दूध-भाजीपाला-फळे") || selectedDepartment.equals("किराना") || selectedDepartment.equals("जुना बाजार")
                || selectedDepartment.equals("जेवण-नाश्ता-टिकावू पदा्रथ")){
            childUpdates.put("/Delivery/"+productID,delivery);
        }
        if(NEW){
            GO_SELLER_APPLICATION.noOfProducts++;
            childUpdates.put("/Sellers/"+GO_SELLER_APPLICATION.sellerID+"/products",GO_SELLER_APPLICATION.noOfProducts);
        }

        reference.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        alert("Product saved successfully");
                        progressBar.setVisibility(View.GONE);
                        getActivity().finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alert("Product upload/update unsuccessful\nTry again ?");
                        GO_SELLER_APPLICATION.noOfProducts--;
                    }
                });

    }


    private boolean validData() {
        boolean valid = false;
        switch (counter){
            case 1 :
                if(selectedDepartment==null|| category==null||subCategory==null){
                    alert("Department/Category/SubCategory not selected !");
                }else{
                    valid = true;
                   /* alert("Department : "+selectedDepartment+"\n"+
                            "Category : "+category+"\n"+
                            "SubCategory : "+subCategory);*/
                    productListing.setDepartment(selectedDepartment);
                    productListing.setDeptCat(selectedDepartment+","+category);
                    productListing.setDeptCatSubCat(selectedDepartment+","+category+","+subCategory);
                }
                break;
            case 2:
                if(     productListing.getProductThumb().matches("x")||
                        productDetails.getUrl1().matches("x")||
                        productDetails.getUrl2().matches("x")||
                        productDetails.getUrl3().matches("x")){
                    alert("All Product photos are required !");
                }else{
                    valid = true;
                }
                break;
            case 3:
                valid = checkData();

                break;
            case 4:
                valid = checkDeliveryData();break;
        }
        return valid;
    }

    private boolean checkDeliveryData() {
        boolean valid = true;
        String time = dtime.getText().toString();
        time.trim();
        if(TextUtils.isEmpty(time)){
            dtime.setError("Required");
            valid = false;
        }else{
            delivery.setTime(time);
            dtime.setError(null);
        }
        String returnP = dreturnPolicy.getText().toString();
        returnP.trim();
        if(TextUtils.isEmpty(returnP)){
            dreturnPolicy.setError("Required");
            valid = false;
        }else{
            delivery.setReturnPolicy(returnP);
            dreturnPolicy.setError(null);
        }
        String charges = dcharges.getText().toString();
        charges.trim();
        if(TextUtils.isEmpty(charges)){
            dcharges.setError("Required");
            valid = false;
        }else{
            int p=0;
            try{
                p = Integer.parseInt(charges);
            }catch (NumberFormatException e){
                dcharges.setError("Enter proper price");
                valid = false;
            }
            delivery.setCharges(p);
            dcharges.setError(null);
        }
        String min = dminOrder.getText().toString();
        min.trim();
        if(TextUtils.isEmpty(min)){
            dminOrder.setError("Required");
            valid = false;
        }else{
            int p=0;
            try{
                p = Integer.parseInt(min);
            }catch (NumberFormatException e){
                dminOrder.setError("Enter proper number");
                valid = false;
            }
            delivery.setMinOrder(p);
            dminOrder.setError(null);
        }
        delivery.setCod(switchCOD.isChecked());
        delivery.setReturnAvailable(switchReturn.isChecked());

        return valid;
    }

    private boolean checkData() {
        boolean valid = true;
        String name = namefield.getText().toString();
        if(TextUtils.isEmpty(name)){
            namefield.setError("Required");
            valid = false;
        }else{
            productListing.setProductName(name);
            namefield.setError(null);
        }
        String description = descriptionfield.getText().toString();
        if(TextUtils.isEmpty(description)){
            descriptionfield.setError("Required");
            valid = false;
        }else{
            productDetails.setDiscription(description);
            descriptionfield.setError(null);
        }

        String mtwo = two.getText().toString();
        if(TextUtils.isEmpty(mtwo)){
            two.setError("Required");
            valid = false;
        }else{
            productDetails.setTwo(mtwo);
            two.setError(null);
        }
        String mthree = three.getText().toString();
        if(TextUtils.isEmpty(mthree)){
            three.setError("Required");
            valid = false;
        }else{
            productDetails.setThree(mthree);
            three.setError(null);
        }
        String mfour = four.getText().toString();
        if(TextUtils.isEmpty(mfour)){
            four.setError("Required");
            valid = false;
        }else{
            productDetails.setFour(mfour);
            four.setError(null);
        }
        String mfive = five.getText().toString();
        if(TextUtils.isEmpty(mfive)){
            five.setError("Required");
            valid = false;
        }else{
            productDetails.setFive(mfive);
            five.setError(null);
        }
        String msix = six.getText().toString();
        if(TextUtils.isEmpty(msix)){
            six.setError("Required");
            valid = false;
        }else{
            productDetails.setSix(msix);
            six.setError(null);
        }

        if(PRICES.isEmpty() && NEW){
            pricesErrorView.setVisibility(View.VISIBLE);
            valid = false;
        }else{
            pricesErrorView.setVisibility(View.GONE);
            if(NEW)productDetails.setPrices(PricesString);
        }
        String mone = one.getText().toString();
            if(TextUtils.isEmpty(mone)){
                one.setError("Required");
                valid = false;
            }else{
                productDetails.setOne(mone);
                one.setError(null);
            }

       /* String price = pricefield.getText().toString();
        if(TextUtils.isEmpty(price)){
            pricefield.setError("Required");
            valid = false;
        }else{
            int p=0;
            try{
                 p = Integer.parseInt(price);
            }catch (NumberFormatException e){
                pricefield.setError("Enter proper price");
                valid = false;
            }*/
           // productListing.setPrice(PRICES.get(0));
            //pricefield.setError(null);

       /* String discount = offerfield.getText().toString();
        if(TextUtils.isEmpty(discount)){
            offerfield.setError("Required");
            valid = false;
        }else{
            int p=0;
            try{
                p = Integer.parseInt(discount);
            }catch (NumberFormatException e){
                offerfield.setError("Enter proper price");
                valid = false;
            }
            productListing.setDiscount(p);
            offerfield.setError(null);
        }
        String quantity = quantityfield.getText().toString();
        if(TextUtils.isEmpty(quantity)){
            quantityfield.setError("Required");
            valid = false;
        }else{
            int p=0;
            try{
                p = Integer.parseInt(quantity);
            }catch (NumberFormatException e){
                quantityfield.setError("Enter proper number");
                valid = false;
            }
            productListing.setqAvailable(p);
            quantityfield.setError(null);
        }*/
        return valid;
    }

    private void alert(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Check this...!")
                .setMessage(s)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(s.matches("You have exhausted your account products limit" +
                                "If you want to upgrade account , go to Profile section")){
                            getActivity().finish();
                        }else if(s.matches("Something went wrong . Please try after some time.")){
                            getActivity().finish();
                        }
                    }
                });
        builder.show();
    }

    private void setUpSpinner1(){
        progressBar.setVisibility(View.VISIBLE);
        reference.child("Departments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            departments.add(snapshot.child("NAME").getValue().toString());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, departments);
                        arrayAdapter.setDropDownViewResource(R.layout.dropdown_layout);

                        spinner1.setEnabled(true);
                        spinner1.setAdapter(arrayAdapter);
                        if(!NEW)spinner1.setSelection(departments.indexOf(productListing.getDepartment()));
                        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedDepartment = parent.getItemAtPosition(position).toString();
                                setUpSpinner2();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                if(NEW)selectedDepartment = parent.getItemAtPosition(0).toString();
                                setUpSpinner2();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setUpSpinner2() {
        showDetails();
        categories.clear();
        if(progressBar.getVisibility() == View.GONE)progressBar.setVisibility(View.VISIBLE);
        reference.child(selectedDepartment).child("Category")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            categories.add(snapshot.child("NAME").getValue().toString());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, categories);
                        arrayAdapter.setDropDownViewResource(R.layout.dropdown_layout);

                        spinner2.setEnabled(true);
                        spinner2.setAdapter(arrayAdapter);
                        if(!NEW)spinner2.setSelection(categories.indexOf(category));
                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                category = parent.getItemAtPosition(position).toString();

                                setUpSpinner3();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                if(NEW)category = parent.getItemAtPosition(0).toString();
                                setUpSpinner3();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setUpSpinner3() {
        subCategories.clear();
        if(progressBar.getVisibility() == View.GONE)progressBar.setVisibility(View.VISIBLE);
        reference.child(selectedDepartment).child("SubCategory").child(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            subCategories.add(snapshot.child("NAME").getValue().toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, subCategories);
                        arrayAdapter.setDropDownViewResource(R.layout.dropdown_layout);

                        spinner3.setEnabled(true);
                        spinner3.setAdapter(arrayAdapter);
                        if(!NEW)spinner3.setSelection(subCategories.indexOf(subCategory));
                        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                subCategory = parent.getItemAtPosition(position).toString();

                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                if(NEW)subCategory = parent.getItemAtPosition(0).toString();

                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    private void toggleLayouts() {
        step1Layout.setVisibility(View.GONE);
        step2Layout.setVisibility(View.GONE);
        step3Layout.setVisibility(View.GONE);
        step4Layout.setVisibility(View.GONE);
        previousButton.setVisibility(View.VISIBLE);
        stepName.setText("Step "+counter+" of "+FINAL_STEP);
        stepTitle.setText(stepTitles[counter]);
        switch (counter){
            case 1 : step1Layout.setVisibility(View.VISIBLE);previousButton.setVisibility(View.INVISIBLE);break;
            case 2 : step2Layout.setVisibility(View.VISIBLE);break;
            case 3 : step3Layout.setVisibility(View.VISIBLE);  break;
            case 4 : step4Layout.setVisibility(View.VISIBLE);break;
        }
    }

    private void showDetails() {

        switch(selectedDepartment){
            case "जेवण-नाश्ता-टिकावू पदा्रथ":
                FINAL_STEP = 4;
                stepProgress.setMax(100);
                attachLabels(
                        "Enter reseller or producer",
                        "Ingredients",
                        "Enter Location where product is produced",
                        "enter expiry if not applicable enter NA",
                        "Enter as Veg or Non-Veg",
                        "Nutritional Benefits else NA"

                );
                break;
            case "ट्रांसपोर्ट":
                FINAL_STEP = 3;
                stepProgress.setMax(75);
                attachLabels(
                        "Enter Registration number of vehicle",
                        "Enter year of model",
                        "Driver driven ? \nY for yes or N for no",
                        "Enter Registration number of vehicle",
                        "Do vehicle has All India Permit ?",
                        "Is vehicle available for emergency night trips"

                );
                break;
            case "दूध-भाजीपाला-फळे":
                FINAL_STEP = 4;
                stepProgress.setMax(100);
                attachLabels(
                        "Are you a reseller or producer",
                        "Are you a reseller or producer",
                        "Enter Location where product is produced",
                        "enter expiry as mentioned on packet if not applicable enter NA",
                        "Subscription Details Enter terms and conditions",
                        "Subscription Prices \nIf reduced prices are available for subscriptions"

                );
                break;
            case "जुना बाजार":
                FINAL_STEP = 4;
                stepProgress.setMax(100);
                attachLabels(
                        "Is original bill available Enter Y or N",
                        "Will you give a certificate of ownership ? Enter Y or N",
                        "Purchase year",
                        "Number of owners",
                        "Reason for sale",
                        "Certified by GO Enter Y/N"

                );
                break;
            case "प्रॉपट्री":
                FINAL_STEP = 3;
                stepProgress.setMax(75);
                attachLabels(
                        "Address of property",
                        "Dimensions of property Enter as sq feets",
                        "Year of construction/purchase",
                        "Type of tenant Office/Family/students/girls etc",
                        "Enter time when you can be contacted",
                        "Deposit/Advance Required Enter amount"

                );
                break;
            case "फॅशन":

                break;
            case "किराना":
                FINAL_STEP = 4;
                stepProgress.setMax(100);
                attachLabels(
                        "Feature",
                        "Feature",
                        "Manufacturer of product",
                        "Expiry if not applicable enter NA",
                        "Veg / Non-Veg",
                        "Feature"           );
                break;
        }

    }

    private void attachLabels(String s,   String s1,  String s2,  String s3, String s4, String s5) {

        two.setHint(s1);
        three.setHint(s2);
        four.setHint(s3);
        five.setHint(s4);
        six.setHint(s5);
        one.setHint(s1);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
    }
}
