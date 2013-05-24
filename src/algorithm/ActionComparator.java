package algorithm;

import java.util.Comparator;

import objects.Action;

final class ActionComparator implements Comparator<Action> {
	@Override
	public int compare(Action arg0, Action arg1) {
		return arg0.getFormalArguments().size() - arg1.getFormalArguments().size();
	}
}