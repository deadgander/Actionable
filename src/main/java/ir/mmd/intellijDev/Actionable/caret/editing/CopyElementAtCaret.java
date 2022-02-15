package ir.mmd.intellijDev.Actionable.caret.editing;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.copyElementAtCaret;
import static ir.mmd.intellijDev.Actionable.caret.editing.Actions.setActionAvailability;

public class CopyElementAtCaret extends AnAction {
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) { copyElementAtCaret(e, false); }
	
	@Override
	public void update(@NotNull AnActionEvent e) { setActionAvailability(e); }
}
