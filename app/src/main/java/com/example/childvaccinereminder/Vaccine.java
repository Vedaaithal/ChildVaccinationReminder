package com.example.childvaccinereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Vaccine extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Versions> versionsList;
    vaccineAdapter vaccineAdapter;
    FloatingActionButton fbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        fbb=findViewById(R.id.fbb);

        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Vaccine.this,MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerVacc);
        versionsList = new ArrayList<>();

        versionsList.add(new Versions("Bacillus Calmette–Guérin (BCG)",
                "Description :\nVaccine for tuberculosis prevention, stimulating the immune system against Mycobacterium tuberculosis.\n"
                        +"Use : \nGiven to infants in high TB incidence areas to prevent tuberculosis.\n"
                        +"Side effects : \nCan cause local skin reactions, swelling, and sometimes lymph node enlargement. Severe reactions are rare.", false));
        versionsList.add(new Versions("Oral polio vaccine (OPV 0-3)",
                "Description :\nOral vaccine for newborns to provide immunity against poliovirus, preventing polio infection.\n"
                        +"Use : \nAdministered to newborns as part of routine immunization to prevent polio.\n"
                        + "Side effects : \nMild reactions may occur, such as fever or temporary gastrointestinal symptoms. Severe reactions are very rare.", false));
        versionsList.add(new Versions("Hepatitis B (Hep B1-B3)",
                "Description :\nVaccine against hepatitis B virus, preventing liver disease by generating immunity to the virus.\n"
                        +"Use : \nGiven to infants and children to prevent hepatitis B infection.\n"
                        + "Side effects : \nGenerally well-tolerated. Possible side effects include pain at the injection site, mild fever, and fatigue. Severe reactions are rare.", false));
        versionsList.add(new Versions("Diptheria, Tetanus, and Pertussis vaccine (DTwP 1-3)",
                "Description :\nCombination vaccine providing protection against diphtheria, tetanus, and pertussis (whooping cough).\n"
                        +"Use : \nProvides immunity against these diseases in infants and children.\n"
                        + "Side effects : \nCommon side effects include fever, pain or swelling at the injection site, and temporary fussiness or drowsiness. Severe reactions are rare.", false));
        versionsList.add(new Versions("Inactivated polio vaccine (IPV 1-3)",
                "Description :\nInjectable vaccine providing immunity against poliovirus, preventing polio infection.\n"
                        +"Use : \nGiven to prevent polio infection in infants and children.\n"
                        + "Side effects : \nMild reactions may occur, such as redness or swelling at the injection site. Severe reactions are rare.", false));
        versionsList.add(new Versions("Haemophilus influenzae type B (Hib 1-3)",
                "Description :\nVaccine preventing Haemophilus influenzae type B infections, including meningitis and pneumonia.\n"
                        +"Use : \nProvides protection against Hib infections in infants and children.\n"
                        + "Side effects : \nGenerally well-tolerated. Possible side effects include redness, swelling, or pain at the injection site. Severe reactions are rare.", false));
        versionsList.add(new Versions("Rotavirus 1-3",
                "Description :\nOral vaccine protecting against rotavirus, the most common cause of severe diarrhea in infants and young children.\n"
                        +"Use : \nGiven to protect against rotavirus infection and reduce the risk of severe diarrhea.\n"
                        + "Side effects : \nMost common side effects include mild diarrhea, irritability, and fever. Severe reactions are rare.", false));
        versionsList.add(new Versions("Pneumococcal conjugate vaccine (PCV 1-3)",
                "Description :\nVaccine preventing pneumococcal diseases like pneumonia, meningitis, and ear infections caused by Streptococcus pneumoniae.\n"
                        + "Use : \nGiven to infants to prevent pneumococcal infections.\n"
                        + "Side effects : \nCommon side effects include redness, swelling, or pain at the injection site. Fever and irritability may also occur. Severe reactions are rare.", false));
        versionsList.add(new Versions("Measles, Mumps, and Rubella (MMR 1-3)",
                "Description :\nVaccine combining measles, mumps, and rubella protection in a single dose\n"
                        +"Use : \nProvides immunity against these three viral infections in infants and children\n"
                        + "Side effects : \nMost common side effects include fever, rash, and temporary joint pain. Severe reactions are rare.", false));
        versionsList.add(new Versions("Typhoid Conjugate Vaccine",
                "Description :\nVaccine offering protection against typhoid fever caused by Salmonella Typhi bacteria.\n"
                        +"Use : \nGiven to individuals traveling to or living in areas with a high risk of typhoid infection.\n"
                        + "Side effects : \nGenerally well-tolerated. Possible side effects include pain at the injection site, fever, headache, and muscle pain. Severe reactions are rare.", false));
        versionsList.add(new Versions("Hepatitis A (Hep A1-A2)",
                "Description :\nVaccine for hepatitis A virus; prevents liver infection.\n"
                        +"Use : \nGiven to provide immunity against hepatitis A in infants and children.\n"
                        + "Side effects : \nCommon side effects include pain at the injection site, headache, fatigue, and loss of appetite. Severe reactions are rare", false));
        versionsList.add(new Versions("Varicella 1-2",
                "Description :\nVaccine to prevent chickenpox caused by the varicella-zoster virus.\n"
                        +"Use : \nAdministered to provide immunity against varicella (chickenpox) in infants and children.\n"
                        + "Side effects : \nCommon side effects include redness, soreness, or swelling at the injection site. Fever or mild rash may also occur. Severe reactions are rare.", false));
        versionsList.add(new Versions("PCV booster",
                "Description :\nBooster dose of the pneumococcal conjugate vaccine for extended protection against pneumococcal diseases.\n"
                        +"Use : \nGiven to reinforce immunity and enhance defense against pneumococcal infections.\n"
                        + "Side effects : \nSimilar to previous doses, common side effects include redness, swelling, or pain at the injection site. Fever and irritability may also occur. Severe reactions are rare.", false));
        versionsList.add(new Versions("Diphtheria, Pertussis, and Tetanus (DTwP B1/DTaP B1-DTwP B2/DTaP B2)",
                "Description :\nInitial dose of the vaccine series to protect against diphtheria, pertussis (whooping cough), and tetanus.\n"
                        +"Use : \nProvides immunity against these bacterial infections and prevents severe complications.\n"
                        + "Side effects : \nSimilar to previous doses, common side effects include fever, pain or swelling at the injection site, and temporary fussiness or drowsiness. Severe reactions are rare.", false));
        versionsList.add(new Versions("Haemophilus influenzae type B (Hib B1)",
                "Description :\nInitial dose of the Haemophilus influenzae type B vaccine series.\n"
                        +"Use : \nProvides further protection against Haemophilus influenzae type B infections.\n"
                        + "Side effects : \nGenerally well-tolerated. Possible side effects include redness, swelling, or pain at the injection site. Severe reactions are rare.", false));
        versionsList.add(new Versions("Booster of Typhoid Conjugate Vaccine",
                "Description :\nAdditional dose of the typhoid conjugate vaccine to maintain protection against typhoid fever.\n"
                        +"Use : \nGiven to individuals at continued risk of typhoid infection.\n"
                        + "Side effects : \nSimilar to previous doses, possible side effects include pain at the injection site, fever, headache, and muscle pain. Severe reactions are rare.", false));


        vaccineAdapter=new vaccineAdapter(versionsList,Vaccine.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(vaccineAdapter);
        vaccineAdapter.notifyDataSetChanged();;

    }

}