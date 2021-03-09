import {Module} from '@src/com/simple/boot/module/Module'

export const Renderer = new class {
    public render(module: Module | string) {
        if (module instanceof Module) {
            document.querySelector('#app')!.innerHTML = module.render();
            module.onChangeRendered();
        } else {
            document.querySelector('#app')!.innerHTML = module;
        }
    }
}()
