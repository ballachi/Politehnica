/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ab4;

import java.util.HashMap;

/**
 *
 * @author marinbulachi
 */
public class Country {
    HashMap<String, County> cities = new HashMap();
    
    // un hash - map care va tine toate judetele

    public HashMap<String, County> getCities() {
        return cities;
    }
}
