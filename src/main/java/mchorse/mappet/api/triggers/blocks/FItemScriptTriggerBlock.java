package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.data.Data;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.ScriptUtils;

import javax.script.ScriptException;

public class FItemScriptTriggerBlock extends ScriptTriggerBlock {
    public void trigger(DataContext context, String uid, Object... args) {
        if (this.inline)
        {
            try
            {
                Mappet.scripts.execute(uid, this.function, context, args);
            }
            catch (ScriptException scriptException)
            {
                Mappet.logger.error(scriptException.getMessage());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        if (!this.string.isEmpty())
        {
            try
            {
                DataContext data = this.apply(context);

                Mappet.scripts.execute(this.string, this.function.trim(), data);

                if (!context.isCanceled())
                {
                    context.cancel(data.isCanceled());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
