package blocks.demo.properties;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import rx.functions.Func1;
import rx.observables.SwingObservable;
import rx.observers.EmptyObserver;
import blocks.core.Property;

public class DemoFrame extends ApplicationFrame {

	public static final String PROP_SEARCH = "search";
	private Property<String> search;

	public static final String PROP_EXECUTE = "execute";
	private Property<String> execute;

	public static final String PROP_RESULTS = "results";
	private Property<List<String>> results;

	public DemoFrame() {
		super("DEMO");

		search = new Property<>(PROP_SEARCH);
		execute = new Property<>(PROP_EXECUTE);
		results = new Property<>(PROP_RESULTS);

		// Création de la fenêtre

		JLabel label = new JLabel("Search :");
		final JTextField textfield = new JTextField();
		JButton button = new JButton("GO");

		JPanel header = new JPanel(new BorderLayout());
		header.add(label, BorderLayout.NORTH);
		header.add(textfield, BorderLayout.CENTER);
		header.add(button, BorderLayout.EAST);

		JList<String> list = new JList<>();
		final DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("test");
		listModel.addElement("test");
		listModel.addElement("test");
		list.setModel(listModel);

		add(header, BorderLayout.NORTH);
		add(list, BorderLayout.CENTER);

		// Bind des properties

		search.listen(SwingObservable.fromFocusEvents(textfield).map(
				new Func1<FocusEvent, String>() {

					@Override
					public String call(FocusEvent focusEvent) {

						System.out.println("SEARCH : " + textfield.getText());

						return textfield.getText();
					}
				}));

		execute.listen(SwingObservable.fromButtonAction(button).map(
				new Func1<ActionEvent, String>() {

					@Override
					public String call(ActionEvent event) {

						System.out.println("BUTTON CLICKED : "
								+ textfield.getText());

						return textfield.getText();
					}

				}));

		results.sendTo(new EmptyObserver<List<String>>() {
			@Override
			public void onNext(List<String> values) {

				System.out.println("RESULTS : ");
				for (String value : values) {
					System.out.println("- " + value);
				}

				listModel.removeAllElements();
				for (String value : values) {
					listModel.addElement(value);
				}
			}
		});

		pack();
		setSize(400, 500);
		RefineryUtilities.centerFrameOnScreen(this);
	}

	public Property<String> searchProperty() {
		return search;
	}

	public Property<String> executeProperty() {
		return execute;
	}

	public Property<List<String>> resultsProperty() {
		return results;
	}

	/**
	 * UID.
	 */
	private static final long serialVersionUID = 5511255292137552252L;

}
