package org.dieschnittstelle.ess.basics;


import org.dieschnittstelle.ess.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.ess.basics.annotations.StockItemProxyImpl;
import org.dieschnittstelle.ess.basics.reflection.ReflectedStockItemBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowAnnotations {

	public static void main(String[] args) {
		// we initialise the collection
		StockItemCollection collection = new StockItemCollection(
				"stockitems_annotations.xml", new AnnotatedStockItemBuilder());
		// we load the contents into the collection
		collection.load();

		for (IStockItem consumable : collection.getStockItems()) {
			showAttributes(((StockItemProxyImpl)consumable).getProxiedObject());
		}

		// we initialise a consumer
		Consumer consumer = new Consumer();
		// ... and let them consume
		consumer.doShopping(collection.getStockItems());
	}

	/*
	 * TODO BAS2
	 */
	private static void showAttributes(Object instance) {
		// TODO BAS2: create a string representation of instance by iterating
		//  over the object's attributes / fields as provided by its class
		//  and reading out the attribute values. The string representation
		//  will then be built from the field names and field values.
		//  Note that only read-access to fields via getters or direct access
		//  is required here.

		//show("class is: " + instance.getClass());
		// Ausgabeformat: {<einfacher Klassenname> <attr1>:<Wert von attr1>, ...}
		// {Milch menge:20, markenname:Mark Brandenburg}
		Class klass = instance.getClass();
		StringBuilder stringAttr = new StringBuilder();

		/*
		// DEBUG
		show("klass.getName() " + klass.getName());
		show("getSimpleName() " + klass.getSimpleName());
		show("getTypeName" + klass.getTypeName());
		*/

		// Nächste schritte!!!!!!!!!!! Methoden holen und getter aufrufen!!!
		Field[] felder = klass.getDeclaredFields();
		String attributeName;
		String getterName;

		stringAttr.append("{" + klass.getSimpleName() + " ");
		for(Field feld: felder)
		{
			//show("Feldername " + feld.getName());
			attributeName = feld.getName();
			getterName = ReflectedStockItemBuilder.getAccessorNameForField("get", attributeName);
			//show(getterName);
			stringAttr.append(feld.getName() + ":");

			try
			{
				//getDeclaredMethod(String name,Class<?>... parameterTypes);
				//Method getterMethod = klass.getDeclaredMethod(getterName, feld.getType());
				// klass.getDeclaredMethods klass.getMethods
				// Unterschied getdeclared liefert alles auch privat!!
				Method getterMethod = klass.getDeclaredMethod(getterName);
				// DEBUG
				//show("getter" + getterMethod);
				//show("Wert" + getterMethod.invoke(instance));
				stringAttr.append(getterMethod.invoke(instance) + ", ");

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		stringAttr.replace(0, stringAttr.length(), stringAttr.substring(0,stringAttr.length()-2)); // Das letze komma ersetzen durch Gesch.klammer
		stringAttr.append("}");
		show(stringAttr);

		// TODO BAS3: if the new @DisplayAs annotation is present on a field,
		//  the string representation will not use the field's name, but the name
		//  specified in the the annotation. Regardless of @DisplayAs being present
		//  or not, the field's value will be included in the string representation.
	}

}
