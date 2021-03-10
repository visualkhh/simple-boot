import Handlebars from 'handlebars'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export class Module {
    constructor(public selector?: string | undefined, public template?: string | undefined) {
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this);
    }

    public onInit() {}
    public onChangeRendered() {}

    // get template(): string {
    //     return ''
    // }

    public render() {
        Renderer.renderTo(this.selector || Renderer.selector, this);
    }

    public renderWrapString(): string {
        // return '-0-';
        if (!this.selector) {
            return Handlebars.compile(this.template)(this);
        } else {
            return `<div id="${this.selector}">${Handlebars.compile(this.template)(this)}</div>`;
        }
    }

    public toString(): string {
        return this.renderWrapString();
        // return '--' + this.renderWrapString() + '--';
    }
}
