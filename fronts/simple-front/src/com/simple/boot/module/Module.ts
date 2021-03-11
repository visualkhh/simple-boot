import Handlebars from 'handlebars'
import {Renderer} from '@src/com/simple/boot/render/Renderer'

export class Module {
    public wrapElement = 'div';
    constructor(public selector?: string | undefined, public template?: string | undefined) {
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this);
    }

    public onInit() {}
    public onChangeRendered() {}

    public render() {
        Renderer.renderTo(this.selector || Renderer.selector, this);
    }

    public renderWrapString(): string {
        if (!this.selector) {
            return Handlebars.compile(this.template)(this);
        } else if (this.selector.startsWith('.')) {
            return `<${this.wrapElement} class="${this.selector.replace('.', ' ')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        } else if (this.selector.startsWith('#')) {
            return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        } else {
            return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        }
    }

    public toString(): string {
        return this.renderWrapString()
    }
}
