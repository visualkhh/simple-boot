import {Module} from '@src/com/simple/boot/module/Module'

export const Renderer = new class {
    public selector = '#app';
    public render(module: Module | string) {
        if (module instanceof Module) {
            document.querySelector(this.selector)!.innerHTML = module.renderString();
            module.onChangeRendered();
        } else {
            document.querySelector(this.selector)!.innerHTML = module;
        }
    }

    public renderTo(selctor: string, module: Module | string) {
        if (module instanceof Module) {
            document.querySelector(selctor)!.innerHTML = module.renderString();
            module.onChangeRendered();
        } else {
            document.querySelector(selctor)!.innerHTML = module;
        }
    }
}()
