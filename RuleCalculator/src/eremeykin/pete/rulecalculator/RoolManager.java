/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.rulecalculator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

/**
 *
 * @author Pete
 */
public class RoolManager {

    private KieSession ksession;
    private FactHandle fact;

    public RoolManager() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        ksession = kc.newKieSession("MainSession");

        InputStream is = null;
        try {

            is = new FileInputStream("C:\\Users\\Pete\\Documents\\NetBeansProjects\\DAS\\RuleCalculator\\src\\eremeykin\\pete\\rulecalculator\\res\\RulesTable.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SpreadsheetCompiler sc = new SpreadsheetCompiler();
        String drl = sc.compile(is, InputType.XLS);
        System.out.println("Generate DRL file is –: ");
        System.out.println(drl);
    }

    public FactHandle insert(Object obj) {
        if (fact == null) {
            fact = ksession.insert(obj);
        }
        ksession.update(fact, obj);
        return fact;
    }

    public void fireAll() {
        ksession.fireAllRules();
        System.out.println("fired");
    }

}
