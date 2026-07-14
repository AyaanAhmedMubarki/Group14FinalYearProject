package com.health.mediconnectx.config;

import com.health.mediconnectx.entity.Medicine;
import com.health.mediconnectx.entity.Role;
import com.health.mediconnectx.repository.MedicineRepository;
import com.health.mediconnectx.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            for (String roleName : List.of("ADMIN", "DOCTOR", "PATIENT")) {
                if (roleRepository.findByName(roleName) == null) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }

    @Bean
    CommandLineRunner seedMedicines(MedicineRepository medicineRepository) {
        return args -> {
            if (medicineRepository.count() > 0) return;

            List<Medicine> medicines = List.of(
                // ── Fever / Pain ──────────────────────────────────────────────────
                med("Calpol 500", "Paracetamol", "GlaxoSmithKline", 15.0, "500mg", "Tablet",
                    "Relieves mild to moderate pain and reduces fever.",
                    "Nausea, rash, liver damage in overdose", "Liver disease, alcohol dependence", 500),
                med("Crocin 500", "Paracetamol", "Haleon", 14.0, "500mg", "Tablet",
                    "Fast-acting relief for fever and pain.",
                    "Nausea, rash", "Severe hepatic impairment", 500),
                med("Dolo 650", "Paracetamol", "Micro Labs", 30.0, "650mg", "Tablet",
                    "Widely used for fever management and body pain.",
                    "Nausea, hepatotoxicity in overdose", "Liver disease", 600),
                med("Disprin", "Aspirin", "Reckitt Benckiser", 12.0, "350mg", "Tablet",
                    "Effervescent tablet for headache, fever, and mild pain.",
                    "GI irritation, bleeding risk", "Peptic ulcer, bleeding disorders, children under 16", 400),
                med("Combiflam", "Ibuprofen + Paracetamol", "Sanofi", 28.0, "400mg + 325mg", "Tablet",
                    "Combination analgesic for pain, inflammation, and fever.",
                    "GI upset, headache, dizziness", "Peptic ulcer, renal impairment", 450),
                med("Voveran 50", "Diclofenac Sodium", "Novartis", 22.0, "50mg", "Tablet",
                    "NSAID for arthritis, musculoskeletal pain, and dysmenorrhea.",
                    "GI pain, nausea, edema", "Peptic ulcer, heart failure, last trimester of pregnancy", 350),
                med("Meftal Spas", "Mefenamic Acid + Dicyclomine", "Blue Cross Labs", 35.0, "250mg + 10mg", "Tablet",
                    "Used for menstrual cramps and abdominal spasms.",
                    "Drowsiness, dry mouth, GI upset", "Glaucoma, obstructive uropathy", 300),
                med("Aceclofenac 100", "Aceclofenac", "Cipla", 30.0, "100mg", "Tablet",
                    "NSAID for osteoarthritis, rheumatoid arthritis, and ankylosing spondylitis.",
                    "GI upset, dizziness, rash", "Peptic ulcer, renal failure, pregnancy", 350),
                med("Meloxicam 15", "Meloxicam", "Boehringer Ingelheim", 35.0, "15mg", "Tablet",
                    "Selective NSAID for osteoarthritis and rheumatoid arthritis.",
                    "GI discomfort, headache, edema", "Peptic ulcer, severe renal impairment", 300),
                med("Tramadol 50", "Tramadol Hydrochloride", "Zydus", 45.0, "50mg", "Tablet",
                    "Opioid analgesic for moderate to severe pain.",
                    "Nausea, dizziness, constipation, dependence risk", "Epilepsy, MAO inhibitor use", 150),

                // ── Antibiotics ───────────────────────────────────────────────────
                med("Augmentin 625", "Amoxicillin + Clavulanate", "GlaxoSmithKline", 120.0, "625mg", "Tablet",
                    "Broad-spectrum antibiotic for respiratory, urinary, and skin infections.",
                    "Diarrhoea, nausea, rash", "Penicillin allergy, jaundice", 250),
                med("Azithral 500", "Azithromycin", "Alembic Pharma", 85.0, "500mg", "Tablet",
                    "Macrolide antibiotic for community-acquired pneumonia, pharyngitis.",
                    "Nausea, diarrhoea, abdominal pain", "Liver disease, QT prolongation", 200),
                med("Cifran 500", "Ciprofloxacin", "Cipla", 60.0, "500mg", "Tablet",
                    "Fluoroquinolone for urinary tract, GI, and respiratory infections.",
                    "Nausea, dizziness, tendon rupture risk", "Children, pregnancy, tendinopathy history", 250),
                med("Roxid 150", "Roxithromycin", "Alembic Pharma", 70.0, "150mg", "Tablet",
                    "Macrolide antibiotic for ENT and respiratory infections.",
                    "GI disturbance, headache", "Liver disease", 180),
                med("Amoxicillin 500", "Amoxicillin", "Cipla", 45.0, "500mg", "Capsule",
                    "Penicillin antibiotic for ear, nose, throat, and chest infections.",
                    "Diarrhoea, rash, nausea", "Penicillin allergy", 300),
                med("Doxycycline 100", "Doxycycline Monohydrate", "Cipla", 55.0, "100mg", "Capsule",
                    "Tetracycline antibiotic for acne, chest, and urinary infections.",
                    "Photosensitivity, nausea, oesophageal irritation", "Pregnancy, children under 8", 200),
                med("Metronidazole 400", "Metronidazole", "Abbott", 18.0, "400mg", "Tablet",
                    "Antibiotic and antiprotozoal for anaerobic infections, H. pylori, amoebiasis.",
                    "Metallic taste, nausea, dark urine", "First trimester of pregnancy, alcohol use", 300),
                med("Clindamycin 300", "Clindamycin Hydrochloride", "Pfizer", 80.0, "300mg", "Capsule",
                    "Antibiotic for skin, dental, and pelvic infections.",
                    "Diarrhoea, Clostridioides difficile colitis", "History of antibiotic-associated colitis", 150),
                med("Nitrofurantoin 100", "Nitrofurantoin", "Cipla", 65.0, "100mg", "Capsule",
                    "Used specifically for urinary tract infections.",
                    "Nausea, headache, pulmonary reactions", "Renal impairment, G6PD deficiency", 180),
                med("Co-trimoxazole 960", "Sulfamethoxazole + Trimethoprim", "Cipla", 22.0, "800mg + 160mg", "Tablet",
                    "Broad-spectrum antibiotic for UTI, respiratory, and GI infections.",
                    "Rash, nausea, Stevens-Johnson syndrome (rare)", "Sulfonamide allergy, severe renal impairment", 200),
                med("Norfloxacin 400", "Norfloxacin", "Cipla", 35.0, "400mg", "Tablet",
                    "Fluoroquinolone antibiotic for urinary and GI tract infections.",
                    "Nausea, headache, dizziness", "Children, pregnancy, tendinopathy", 200),

                // ── Gastric / GI ──────────────────────────────────────────────────
                med("Pantop 40", "Pantoprazole", "Aristo Pharma", 55.0, "40mg", "Tablet",
                    "Proton pump inhibitor for acid reflux, GERD, and peptic ulcers.",
                    "Headache, diarrhoea, nausea", "Hypersensitivity to pantoprazole", 400),
                med("Pan D", "Pantoprazole + Domperidone", "Alkem Labs", 65.0, "40mg + 10mg", "Capsule",
                    "Combination for GERD with delayed gastric emptying and nausea.",
                    "Dry mouth, headache, diarrhoea", "GI obstruction, prolactinoma", 300),
                med("Omez 20", "Omeprazole", "Dr. Reddy's", 40.0, "20mg", "Capsule",
                    "Reduces stomach acid for ulcers and acid reflux.",
                    "Headache, flatulence, nausea", "Severe liver disease", 350),
                med("Rantac 150", "Ranitidine", "J.B. Chemicals", 25.0, "150mg", "Tablet",
                    "H2-blocker for peptic ulcer disease and hyperacidity.",
                    "Headache, constipation", "Porphyria", 400),
                med("Esomeprazole 40", "Esomeprazole", "AstraZeneca", 65.0, "40mg", "Capsule",
                    "PPI for erosive oesophagitis, GERD, and H. pylori eradication.",
                    "Headache, diarrhoea, nausea", "Hypersensitivity to PPIs", 350),
                med("Ondansetron 4", "Ondansetron", "GlaxoSmithKline", 35.0, "4mg", "Tablet",
                    "Antiemetic for nausea and vomiting due to chemotherapy, surgery, or illness.",
                    "Headache, constipation, QT prolongation", "QT prolongation, congenital long QT syndrome", 300),
                med("Domperidone 10", "Domperidone", "Cipla", 20.0, "10mg", "Tablet",
                    "Promotes gastric motility; relieves nausea, bloating, and delayed gastric emptying.",
                    "Dry mouth, headache, prolactin elevation", "GI obstruction, prolactinoma", 400),
                med("Sucralfate 1g", "Sucralfate", "Sun Pharma", 28.0, "1g", "Tablet",
                    "Mucosal protective agent for peptic and duodenal ulcers.",
                    "Constipation, dry mouth", "Renal failure (aluminium toxicity risk)", 250),
                med("Lactulose Syrup", "Lactulose", "Abbott", 85.0, "10g/15ml", "Syrup",
                    "Osmotic laxative for constipation and hepatic encephalopathy.",
                    "Bloating, flatulence, diarrhoea in excess", "Galactosaemia, intestinal obstruction", 150),

                // ── Allergy / Cold / Cough ────────────────────────────────────────
                med("Allegra 120", "Fexofenadine", "Sanofi", 55.0, "120mg", "Tablet",
                    "Non-sedating antihistamine for seasonal allergic rhinitis and urticaria.",
                    "Headache, nausea, mild drowsiness", "Severe renal impairment", 300),
                med("Cetirizine 10", "Cetirizine", "Cipla", 18.0, "10mg", "Tablet",
                    "Antihistamine for allergic rhinitis, urticaria, and itching.",
                    "Drowsiness, dry mouth, fatigue", "Renal impairment (dose adjustment needed)", 500),
                med("Montek LC", "Montelukast + Levocetirizine", "Sun Pharma", 65.0, "10mg + 5mg", "Tablet",
                    "Used for chronic allergic rhinitis and allergic asthma.",
                    "Drowsiness, dry mouth", "Severe renal impairment", 250),
                med("Loratadine 10", "Loratadine", "Cipla", 22.0, "10mg", "Tablet",
                    "Non-drowsy antihistamine for hay fever and urticaria.",
                    "Headache, dry mouth, fatigue", "Severe hepatic impairment", 300),
                med("Chlorpheniramine 4", "Chlorpheniramine Maleate", "Micro Labs", 10.0, "4mg", "Tablet",
                    "First-generation antihistamine for allergic rhinitis and urticaria.",
                    "Drowsiness, dry mouth, blurred vision", "Glaucoma, prostatic hypertrophy", 500),
                med("Sinarest", "Paracetamol + Phenylephrine + Chlorphenamine", "Centaur Pharma", 22.0, "325mg + 5mg + 2mg", "Tablet",
                    "Multi-symptom relief for cold — congestion, fever, and sneezing.",
                    "Drowsiness, dry mouth, blurred vision", "Hypertension, MAO inhibitor use", 400),
                med("D-Cold Total", "Paracetamol + Phenylephrine + Caffeine + Chlorphenamine", "Novartis", 25.0, "500mg + 10mg + 30mg + 2mg", "Tablet",
                    "Relieves symptoms of common cold including headache and nasal congestion.",
                    "Drowsiness, palpitations, dry mouth", "Hypertension, heart disease", 350),
                med("Benadryl Cough Syrup", "Diphenhydramine Citrate + Ammonium Chloride", "Johnson & Johnson", 88.0, "14.08mg/5ml", "Syrup",
                    "Provides relief from dry and productive cough.",
                    "Drowsiness, dry mouth, dizziness", "Children under 2, glaucoma, asthma", 200),
                med("Alex Cough Syrup", "Chlorpheniramine + Dextromethorphan + Guaifenesin", "Glenmark", 72.0, "2mg + 10mg + 100mg per 5ml", "Syrup",
                    "Combination cough syrup for dry and wet cough.",
                    "Drowsiness, nausea", "Children under 6, MAO inhibitor use", 150),

                // ── Respiratory ───────────────────────────────────────────────────
                med("Asthalin Inhaler", "Salbutamol", "Cipla", 85.0, "100mcg/dose", "Inhaler",
                    "Reliever inhaler for acute bronchospasm in asthma and COPD.",
                    "Tremor, palpitations, headache", "Hypersensitivity to salbutamol", 200),
                med("Asthalin 4mg", "Salbutamol Sulphate", "Cipla", 22.0, "4mg", "Tablet",
                    "Oral bronchodilator for asthma and COPD maintenance.",
                    "Tremor, tachycardia, headache", "Hyperthyroidism, severe cardiac disease", 250),
                med("Montelukast 10", "Montelukast", "Sun Pharma", 45.0, "10mg", "Tablet",
                    "Leukotriene receptor antagonist for asthma prophylaxis and allergic rhinitis.",
                    "Headache, abdominal pain, mood changes (rare)", "None significant", 300),
                med("Doxofylline 400", "Doxofylline", "Cipla", 55.0, "400mg", "Tablet",
                    "Xanthine bronchodilator for bronchial asthma and COPD.",
                    "Nausea, palpitations, headache", "Acute myocardial infarction, hypotension", 200),
                med("Pregabalin 75", "Pregabalin", "Sun Pharma", 90.0, "75mg", "Capsule",
                    "Used for neuropathic pain, fibromyalgia, and partial-onset seizures.",
                    "Dizziness, somnolence, weight gain", "Renal impairment (dose reduction)", 200),
                med("Gabapentin 300", "Gabapentin", "Pfizer", 75.0, "300mg", "Capsule",
                    "Anticonvulsant for epilepsy and neuropathic pain.",
                    "Dizziness, somnolence, ataxia", "Renal impairment (dose reduction)", 200),

                // ── Diabetes ──────────────────────────────────────────────────────
                med("Glycomet 500", "Metformin", "USV Pharma", 35.0, "500mg", "Tablet",
                    "First-line oral antidiabetic for type 2 diabetes mellitus.",
                    "GI upset, lactic acidosis (rare)", "Renal impairment, liver disease, contrast media use", 400),
                med("Glipizide 5", "Glipizide", "Pfizer", 28.0, "5mg", "Tablet",
                    "Sulfonylurea for blood glucose control in type 2 diabetes.",
                    "Hypoglycaemia, weight gain, nausea", "Type 1 diabetes, sulfonamide allergy", 250),
                med("Glibenclamide 5", "Glibenclamide", "Cipla", 22.0, "5mg", "Tablet",
                    "Long-acting sulfonylurea for type 2 diabetes.",
                    "Hypoglycaemia, weight gain", "Renal/hepatic failure, elderly", 250),
                med("Sitagliptin 100", "Sitagliptin", "Merck", 180.0, "100mg", "Tablet",
                    "DPP-4 inhibitor for type 2 diabetes; improves glycaemic control.",
                    "Nasopharyngitis, headache, upper respiratory infection", "Type 1 diabetes, DKA", 150),
                med("Lantus (Insulin Glargine)", "Insulin Glargine", "Sanofi", 850.0, "100 IU/mL", "Injection",
                    "Long-acting basal insulin for type 1 and type 2 diabetes.",
                    "Hypoglycaemia, injection site reactions, weight gain", "Hypoglycaemia episodes", 100),
                med("Actrapid (Insulin Regular)", "Insulin Regular", "Novo Nordisk", 280.0, "40 IU/mL", "Injection",
                    "Short-acting insulin for meal-time blood glucose control.",
                    "Hypoglycaemia, weight gain, lipodystrophy", "Hypoglycaemia", 100),

                // ── Cardiovascular / BP ───────────────────────────────────────────
                med("Amlodipine 5", "Amlodipine Besylate", "Cipla", 22.0, "5mg", "Tablet",
                    "Calcium channel blocker for hypertension and angina.",
                    "Peripheral oedema, flushing, dizziness", "Cardiogenic shock, aortic stenosis", 350),
                med("Telmisartan 40", "Telmisartan", "Cipla", 45.0, "40mg", "Tablet",
                    "ARB for hypertension and cardiovascular risk reduction.",
                    "Dizziness, diarrhoea, hyperkalaemia", "Pregnancy, renal artery stenosis", 300),
                med("Atorvastatin 10", "Atorvastatin", "Ranbaxy", 38.0, "10mg", "Tablet",
                    "Statin for lowering LDL cholesterol and preventing cardiovascular events.",
                    "Myopathy, elevated liver enzymes", "Active liver disease, pregnancy", 350),
                med("Metoprolol 25", "Metoprolol Succinate", "AstraZeneca", 30.0, "25mg", "Tablet",
                    "Beta-blocker for hypertension, angina, and heart failure.",
                    "Bradycardia, fatigue, cold extremities", "Asthma, severe bradycardia, cardiogenic shock", 300),
                med("Atenolol 50", "Atenolol", "Cipla", 18.0, "50mg", "Tablet",
                    "Cardioselective beta-blocker for hypertension and angina.",
                    "Bradycardia, fatigue, cold extremities", "Asthma, heart block", 350),
                med("Ramipril 5", "Ramipril", "Aventis", 40.0, "5mg", "Tablet",
                    "ACE inhibitor for hypertension, heart failure, and post-MI.",
                    "Dry cough, hyperkalaemia, dizziness", "Pregnancy, history of angioedema", 300),
                med("Aspirin 75", "Aspirin (Low-dose)", "Bayer", 8.0, "75mg", "Tablet",
                    "Antiplatelet for prevention of heart attacks and strokes.",
                    "GI bleeding, bruising", "Active peptic ulcer, bleeding disorders", 500),
                med("Ecosprin 75", "Aspirin", "USV Pharma", 7.0, "75mg", "Tablet",
                    "Enteric-coated low-dose aspirin for antiplatelet therapy.",
                    "GI irritation, bleeding risk", "Peptic ulcer, aspirin allergy", 500),
                med("Clopidogrel 75", "Clopidogrel", "Sun Pharma", 65.0, "75mg", "Tablet",
                    "Antiplatelet agent to prevent blood clots in coronary artery disease.",
                    "Bleeding, bruising, GI upset", "Active bleeding, peptic ulcer", 250),
                med("Isosorbide Mononitrate 10", "Isosorbide Mononitrate", "Sun Pharma", 30.0, "10mg", "Tablet",
                    "Nitrate for prevention and treatment of angina pectoris.",
                    "Headache, hypotension, flushing", "Severe hypotension, hypovolaemia", 200),

                // ── CNS / Psychiatry ──────────────────────────────────────────────
                med("Diazepam 5", "Diazepam", "Roche", 12.0, "5mg", "Tablet",
                    "Benzodiazepine for anxiety, muscle spasm, and alcohol withdrawal.",
                    "Drowsiness, dependence, respiratory depression", "Respiratory depression, myasthenia gravis", 100),
                med("Alprazolam 0.25", "Alprazolam", "Pfizer", 18.0, "0.25mg", "Tablet",
                    "Short-acting benzodiazepine for anxiety and panic disorder.",
                    "Drowsiness, dependence, cognitive impairment", "Respiratory insufficiency, severe hepatic disease", 100),
                med("Sertraline 50", "Sertraline", "Pfizer", 55.0, "50mg", "Tablet",
                    "SSRI antidepressant for depression, OCD, panic disorder, and PTSD.",
                    "Nausea, insomnia, sexual dysfunction, suicidality risk", "MAO inhibitor use within 14 days", 200),
                med("Amitriptyline 25", "Amitriptyline", "Sun Pharma", 20.0, "25mg", "Tablet",
                    "Tricyclic antidepressant for depression and neuropathic pain.",
                    "Dry mouth, sedation, constipation, urinary retention", "Recent MI, arrhythmia, MAO inhibitor", 150),
                med("Escitalopram 10", "Escitalopram", "Sun Pharma", 60.0, "10mg", "Tablet",
                    "SSRI for major depressive disorder and generalized anxiety disorder.",
                    "Nausea, insomnia, headache, sexual dysfunction", "MAO inhibitor use within 14 days", 200),
                med("Phenytoin 100", "Phenytoin Sodium", "Pfizer", 30.0, "100mg", "Tablet",
                    "Anticonvulsant for tonic-clonic and partial seizures.",
                    "Gingival hyperplasia, ataxia, rash, Stevens-Johnson syndrome", "Sinus bradycardia, heart block", 150),

                // ── Thyroid ───────────────────────────────────────────────────────
                med("Eltroxin 50", "Levothyroxine", "GlaxoSmithKline", 35.0, "50mcg", "Tablet",
                    "Thyroid hormone replacement for hypothyroidism.",
                    "Palpitations, weight loss, insomnia in overdose", "Hyperthyroidism, recent MI", 300),
                med("Methimazole 10", "Methimazole", "Sun Pharma", 28.0, "10mg", "Tablet",
                    "Antithyroid drug for hyperthyroidism and Graves' disease.",
                    "Rash, agranulocytosis (rare), hypothyroidism", "Pregnancy (prefer PTU in 1st trimester)", 150),

                // ── Steroids ──────────────────────────────────────────────────────
                med("Prednisolone 10", "Prednisolone", "Sun Pharma", 18.0, "10mg", "Tablet",
                    "Corticosteroid for inflammatory, allergic, and autoimmune conditions.",
                    "Weight gain, hyperglycaemia, osteoporosis, immunosuppression", "Active infections, live vaccines", 300),
                med("Dexamethasone 0.5", "Dexamethasone", "Sun Pharma", 12.0, "0.5mg", "Tablet",
                    "Potent corticosteroid for inflammatory conditions and cerebral oedema.",
                    "Cushing syndrome, hyperglycaemia, immunosuppression", "Systemic fungal infection", 200),
                med("Methylprednisolone 4", "Methylprednisolone", "Pfizer", 22.0, "4mg", "Tablet",
                    "Corticosteroid for acute inflammatory and allergic conditions.",
                    "Hyperglycaemia, insomnia, weight gain", "Active infections, live vaccines", 200),

                // ── Vitamins / Supplements ────────────────────────────────────────
                med("Becosules", "Vitamin B Complex + Vitamin C", "Pfizer", 60.0, "Standard", "Capsule",
                    "Nutritional supplement for Vitamin B and C deficiencies.",
                    "Flushing, GI upset in high doses", "None significant", 500),
                med("Revital H", "Multivitamin + Minerals + Ginseng", "Ranbaxy", 180.0, "Standard", "Capsule",
                    "Daily health supplement to improve energy and vitality.",
                    "Mild GI discomfort", "Children, pregnancy (consult doctor)", 300),
                med("Zincovit", "Zinc + Multivitamins", "Apex Laboratories", 90.0, "Standard", "Tablet",
                    "Zinc and vitamin supplement for immunity and metabolism.",
                    "Mild nausea", "None significant", 400),
                med("Ferrous Sulphate 200", "Ferrous Sulphate", "Cipla", 20.0, "200mg", "Tablet",
                    "Iron supplement for iron-deficiency anaemia.",
                    "Constipation, dark stools, nausea", "Haemochromatosis, haemolytic anaemia", 400),
                med("Folic Acid 5", "Folic Acid", "Sun Pharma", 8.0, "5mg", "Tablet",
                    "Vitamin B9 supplement; essential in pregnancy to prevent neural tube defects.",
                    "Rare GI upset at high doses", "None significant", 500),
                med("Methylcobalamin 500", "Methylcobalamin (Vitamin B12)", "Sun Pharma", 50.0, "500mcg", "Tablet",
                    "Active form of Vitamin B12 for neuropathy and B12 deficiency.",
                    "Rare: mild GI upset", "None significant", 400),
                med("Shelcal 500", "Calcium Carbonate + Vitamin D3", "Torrent", 55.0, "500mg + 250 IU", "Tablet",
                    "Calcium supplement for osteoporosis prevention and bone health.",
                    "Constipation, hypercalcaemia in excess", "Hypercalcaemia, kidney stones", 400),
                med("Vitamin D3 60000 IU", "Cholecalciferol", "Sun Pharma", 35.0, "60000 IU", "Capsule",
                    "Weekly Vitamin D3 supplement for deficiency correction.",
                    "Hypercalcaemia in overdose", "Hypercalcaemia, hypervitaminosis D", 300),
                med("Vitamin C 500", "Ascorbic Acid", "Cipla", 15.0, "500mg", "Tablet",
                    "Antioxidant supplement for Vitamin C deficiency and immune support.",
                    "GI upset, kidney stones in excess", "History of oxalate kidney stones", 500),

                // ── Antifungal / Antiparasitic ─────────────────────────────────────
                med("Fluconazole 150", "Fluconazole", "Cipla", 28.0, "150mg", "Capsule",
                    "Single-dose oral antifungal for vaginal candidiasis and thrush.",
                    "Nausea, headache, rash", "Liver disease, QT prolongation, cisapride use", 200),
                med("Itraconazole 200", "Itraconazole", "Janssen", 95.0, "200mg", "Capsule",
                    "Broad-spectrum antifungal for dermatophytosis, candidiasis, and aspergillosis.",
                    "Nausea, hepatotoxicity, heart failure risk", "Heart failure, ventricular dysfunction", 150),
                med("Albendazole 400", "Albendazole", "GSK", 25.0, "400mg", "Tablet",
                    "Antiparasitic for roundworm, hookworm, tapeworm, and cysticercosis.",
                    "Abdominal pain, nausea, elevated liver enzymes", "Pregnancy (first trimester)", 300),
                med("Ivermectin 12", "Ivermectin", "Sun Pharma", 55.0, "12mg", "Tablet",
                    "Antiparasitic for scabies, head lice, strongyloidiasis, and onchocerciasis.",
                    "Pruritus, dizziness, nausea", "Children under 15 kg, pregnancy", 200),

                // ── Diuretics ─────────────────────────────────────────────────────
                med("Furosemide 40", "Furosemide", "Sanofi", 15.0, "40mg", "Tablet",
                    "Loop diuretic for oedema in heart failure, renal disease, and hypertension.",
                    "Hypokalaemia, dehydration, ototoxicity (high doses)", "Anuria, sulfonamide allergy", 300),
                med("Spironolactone 25", "Spironolactone", "Sun Pharma", 22.0, "25mg", "Tablet",
                    "Potassium-sparing diuretic for heart failure, oedema, and hyperaldosteronism.",
                    "Hyperkalaemia, gynaecomastia, menstrual irregularity", "Hyperkalaemia, Addison's disease", 250),

                // ── Urology ───────────────────────────────────────────────────────
                med("Tamsulosin 0.4", "Tamsulosin Hydrochloride", "Ranbaxy", 65.0, "0.4mg", "Capsule",
                    "Alpha-blocker for benign prostatic hyperplasia (BPH).",
                    "Dizziness, retrograde ejaculation, orthostatic hypotension", "Severe hepatic impairment", 200),

                // ── Topical ───────────────────────────────────────────────────────
                med("Betadine 5%", "Povidone-Iodine", "Win-Medicare", 45.0, "5%", "Solution",
                    "Antiseptic solution for wound cleaning and pre-operative skin disinfection.",
                    "Skin irritation, iodine toxicity in large wounds", "Thyroid disorders, iodine allergy", 200),
                med("Volini Gel", "Diclofenac + Methyl Salicylate + Menthol", "Ranbaxy", 75.0, "1% + 10% + 5%", "Gel",
                    "Topical analgesic for muscle pain, sprains, and joint inflammation.",
                    "Skin irritation, redness", "Open wounds, eczema, aspirin allergy", 250),
                med("Soframycin Cream", "Framycetin Sulphate", "Sanofi", 55.0, "1%", "Cream",
                    "Topical antibiotic cream for infected skin wounds and burns.",
                    "Skin sensitization with prolonged use", "Large open wounds, neomycin allergy", 200),
                med("Hydrocortisone 1% Cream", "Hydrocortisone", "Cipla", 40.0, "1%", "Cream",
                    "Mild corticosteroid for eczema, dermatitis, and insect bites.",
                    "Skin thinning with prolonged use", "Infected skin, rosacea, acne", 200),
                med("Clotrimazole 1% Cream", "Clotrimazole", "Bayer", 45.0, "1%", "Cream",
                    "Antifungal cream for ringworm, athlete's foot, and candidal skin infections.",
                    "Skin irritation, burning", "Hypersensitivity to clotrimazole", 250),

                // ── Eye / Ear ─────────────────────────────────────────────────────
                med("Ciprofloxacin Eye Drops 0.3%", "Ciprofloxacin", "Sun Pharma", 35.0, "0.3%", "Eye Drops",
                    "Antibiotic eye drops for bacterial conjunctivitis and corneal ulcers.",
                    "Burning, stinging on instillation", "Contact lens use, viral/fungal eye infections", 200),

                // ── ORS / Electrolytes ────────────────────────────────────────────
                med("ORS Sachet", "Oral Rehydration Salts", "Alkem Labs", 10.0, "One sachet in 200ml water", "Powder",
                    "Oral rehydration for dehydration due to diarrhoea, vomiting, or sweating.",
                    "Hypernatraemia in excess", "Severe vomiting preventing oral intake", 500),
                med("Hydroxychloroquine 200", "Hydroxychloroquine", "Ipca", 55.0, "200mg", "Tablet",
                    "Used for malaria, rheumatoid arthritis, and systemic lupus erythematosus.",
                    "GI upset, retinopathy (long-term), QT prolongation", "Retinal disease, G6PD deficiency", 150)
            );

            medicineRepository.saveAll(medicines);
        };
    }

    private static Medicine med(String name, String genericName, String manufacturer,
                                 double price, String dosage, String type,
                                 String description, String sideEffects,
                                 String contraindications, int qty) {
        Medicine m = new Medicine();
        m.setName(name);
        m.setGenericName(genericName);
        m.setManufacturer(manufacturer);
        m.setPrice(price);
        m.setDosage(dosage);
        m.setType(type);
        m.setDescription(description);
        m.setSideEffects(sideEffects);
        m.setContraindications(contraindications);
        m.setStockQuantity(qty);
        return m;
    }
}
