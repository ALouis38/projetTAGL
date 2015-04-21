package fr.gausta.dataagregator;

import java.util.HashMap;

public interface DataFinderIntf {
	HashMap<String, String> findData(String query);
}
