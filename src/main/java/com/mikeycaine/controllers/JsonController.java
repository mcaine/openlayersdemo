package com.mikeycaine.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonController {
	
	@RequestMapping("/pointdata")
	public String jsondata() throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException {
		Map<String, Point> pointMap = new HashMap<String, Point>();
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point admiralTowerPoint = geometryFactory.createPoint(new Coordinate(537749.85, 177861.19));
		Point barrowGatePoint = geometryFactory.createPoint(new Coordinate(326865, 520251));
		
		pointMap.put("Admiral's Tower", admiralTowerPoint);
		pointMap.put("Barrow Gate", barrowGatePoint);
		
		return jsonFeatureCollectionFromPointMap("EPSG:27700", pointMap);
	}
	
	@RequestMapping("/polygondata")
	public String polygondata() throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException {
		Map<String, MultiPolygon> multiPolygonMap = new HashMap<String, MultiPolygon>();
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		MultiPolygon japanMultiPolygon = geometryFactory.createMultiPolygon(japanPolygons());
		
		multiPolygonMap.put("Japan", japanMultiPolygon);
		
		return jsonFeatureCollectionFromMultiPolygonMap("EPSG:4326", multiPolygonMap);
	}
	
	private Polygon[] japanPolygons() {
		double[][][][] data = {
	    				{{{134.638428,34.149234},{134.766379,33.806335},{134.203416,33.201178},{133.79295,33.521985},{133.280268,33.28957},{133.014858,32.704567},
	      			    {132.363115,32.989382},{132.371176,33.463642},{132.924373,34.060299},{133.492968,33.944621},{133.904106,34.364931},{134.638428,34.149234}}},
	      			    {{{140.976388,37.142074},{140.59977,36.343983},{140.774074,35.842877},{140.253279,35.138114},{138.975528,34.6676},{137.217599,34.606286},
	      			    {135.792983,33.464805},{135.120983,33.849071},{135.079435,34.596545},{133.340316,34.375938},{132.156771,33.904933},{130.986145,33.885761},
	      			    {132.000036,33.149992},{131.33279,31.450355},{130.686318,31.029579},{130.20242,31.418238},{130.447676,32.319475},{129.814692,32.61031},
	      			    {129.408463,33.296056},{130.353935,33.604151},{130.878451,34.232743},{131.884229,34.749714},{132.617673,35.433393},{134.608301,35.731618},
	      			    {135.677538,35.527134},{136.723831,37.304984},{137.390612,36.827391},{138.857602,37.827485},{139.426405,38.215962},{140.05479,39.438807},
	      			    {139.883379,40.563312},{140.305783,41.195005},{141.368973,41.37856},{141.914263,39.991616},{141.884601,39.180865},{140.959489,38.174001},
	      			    {140.976388,37.142074}}},{{{143.910162,44.1741},{144.613427,43.960883},{145.320825,44.384733},{145.543137,43.262088},{144.059662,42.988358},
	      			    {143.18385,41.995215},{141.611491,42.678791},{141.067286,41.584594},{139.955106,41.569556},{139.817544,42.563759},
	      			    {140.312087,43.333273},{141.380549,43.388825},{141.671952,44.772125},{141.967645,45.551483},{143.14287,44.510358},
	      			    {143.910162,44.1741}}}};
		
	    org.locationtech.jts.geom.GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
	    
	    List<Polygon> polygonList = new ArrayList<>();
	    for (int i = 0; i < data.length; ++i) {
	    	for (int j = 0; j < data[i].length; ++j) {
	    		Coordinate[] coordinates = new Coordinate[data[i][j].length];
	    		for (int k = 0; k < data[i][j].length; ++k) {
	    			coordinates[k] = new Coordinate(data[i][j][k][0], data[i][j][k][1]);
	    		}
	    		Polygon polygon = geometryFactory.createPolygon(coordinates);
	    		polygonList.add(polygon);
	    	}
	    }
	    
	    Polygon[] polygons = new Polygon[polygonList.size()];
	    for (int i = 0; i < polygonList.size(); ++i) {
	    	polygons[i] = polygonList.get(i);
	    }
	    return polygons;
	}
	
	private String jsonFeatureCollectionFromPointMap(String crs, Map<String, Point> pointMap) throws NoSuchAuthorityCodeException, FactoryException, IOException {
		
	    CoordinateReferenceSystem osgbCrs = CRS.decode(crs);
		
		SimpleFeatureTypeBuilder pointTypeBuilder = new SimpleFeatureTypeBuilder();
		pointTypeBuilder.setName("Location");
		pointTypeBuilder.setCRS(osgbCrs);
		pointTypeBuilder.add("location", Point.class);
		pointTypeBuilder.add("name", String.class);
		
		final SimpleFeatureType pointType = pointTypeBuilder.buildFeatureType();
		
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null,null);
		
		for (Map.Entry<String, Point> entry : pointMap.entrySet()) {
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(pointType);
			featureBuilder.add(entry.getValue());
			featureBuilder.add(entry.getKey());
			SimpleFeature feature = featureBuilder.buildFeature(null);
			featureCollection.add(feature);
		}
		
	    FeatureJSON fj = new FeatureJSON();
	    fj.setEncodeFeatureCRS(true);
	    
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    fj.writeFeatureCollection(featureCollection, os);
		
		return os.toString();
	}
	
	private String jsonFeatureCollectionFromMultiPolygonMap(String crs, Map<String, MultiPolygon> multiPolygonMap) throws NoSuchAuthorityCodeException, FactoryException, IOException {
		
	    CoordinateReferenceSystem osgbCrs = CRS.decode(crs);
		
		SimpleFeatureTypeBuilder multipolygonTypeBuilder = new SimpleFeatureTypeBuilder();
		multipolygonTypeBuilder.setName("Country");
		multipolygonTypeBuilder.setCRS(osgbCrs);
		multipolygonTypeBuilder.add("countryPolygon", MultiPolygon.class);
		multipolygonTypeBuilder.add("name", String.class);
		
		final SimpleFeatureType pointType = multipolygonTypeBuilder.buildFeatureType();
		
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(null,null);
		
		for (Map.Entry<String, MultiPolygon> entry : multiPolygonMap.entrySet()) {
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(pointType);
			featureBuilder.add(entry.getValue());
			featureBuilder.add(entry.getKey());
			SimpleFeature feature = featureBuilder.buildFeature(null);
			featureCollection.add(feature);
		}
		
	    FeatureJSON fj = new FeatureJSON();
	    fj.setEncodeFeatureCRS(true);
	    
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    fj.writeFeatureCollection(featureCollection, os);
		
		return os.toString();
	}
}
