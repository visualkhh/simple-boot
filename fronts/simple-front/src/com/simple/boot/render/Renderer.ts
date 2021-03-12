import {Module} from '@src/com/simple/boot/module/Module'
import Handlebars from 'handlebars'

export const Renderer = new class {
    public selector = '#app';

    public renderString(template: string, obj: any): string {
        return Handlebars.compile(template)(obj);
    }

    public render(module: Module | string) {
        if (module instanceof Module) {
            document.querySelector(this.selector)!.innerHTML = module.renderString();
            module.onChangedRendered();
        } else {
            document.querySelector(this.selector)!.innerHTML = module;
        }
    }

    public renderTo(selctor: string, module: Module | string) {
        const querySelector = document.querySelector(`#${selctor}`)
        if (querySelector) {
            if (module instanceof Module) {
                querySelector.innerHTML = module.renderString();
                module.onChangedRendered();
            } else {
                querySelector.innerHTML = module;
            }
        }
    }
}()
