import Handlebars from 'handlebars'
import {Renderer} from '../../../../com/simple/boot/render/Renderer'
import {LifeCycle} from '@src/com/simple/boot/module/LifeCycle'

const {v4: uuidv4} = require('uuid')

// export type StyleType = [[string, string, string, any]];
// export enum ImportMode {
//     src,
//     raw
// }
// export enum ImportTriggerMode {
//     init,
//     changedRendered,
//     initedChiled,
//     finish,
// }
// export class Import {
//     constructor(public source: string, public triggerMode = ImportTriggerMode.init) {
//     }
//
//     // public getSource(): Promise<string> {
//     //     if (this.source instanceof Promise) {
//     //         return this.source;
//     //     } else {
//     //         return new Promise((resolve, reject) => {
//     //             resolve(this.source);
//     //         });
//     //     }
//     // }
// }

export class Module implements LifeCycle {
    // constructor(public selector?: string | undefined, public template?: string | undefined) {
    public router_outlet_selector: string | undefined
    public styleImports: string[] | undefined

    constructor(public selector = '', public template = '', public wrapElement = 'div') {
        this.selector = `___Module___${this.selector}_${uuidv4()}`
        if (this.template.search('\\[router-outlet\\]')) {
            this.router_outlet_selector = `___Module___router-outlet_${this.selector}_${uuidv4()}`
            this.template = this.template.replace('[router-outlet]', ` id='${this.router_outlet_selector}' `)
        }
        console.log('module constructor, ', 'selector', selector, 'isProxy' in this)
    }

    public renderString(): string {
        return Handlebars.compile(this.template)(this)
    }

    public privateInit() {
        Renderer.renderTo(this.selector, '')
        this.onInit()
    }

    public privateChangedRendered() {
        this.onChangedRendered()
    }

    public privateInitedChiled() {
        this.onInitedChiled()
    }

    public privateFinish() {
        this.onFinish()
    }

    public onInit() {
    }

    public onChangedRendered() {
    }

    public onInitedChiled() {
    }

    public onFinish() {
    }

    public render(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this)
        this.transStyle(this.selector);
    }

    public renderWrap(selector = this.selector || Renderer.selector) {
        Renderer.renderTo(selector, this.renderWrapString())
        this.privateChangedRendered();
        this.transStyle(this.selector);
    }

    public transStyle(selector: string): void {
        const join = this.styleImports?.map(it => {
            // eslint-disable-next-line prefer-regex-literals
            const regExp = new RegExp('\\/\\*\\[module\\-selector\\]\\*\\/', 'gi') // 생성자
            return it.replace(regExp, '#' + selector + ' ');
        }).join(' ');
        console.log('---transStyle->', selector)
        Renderer.prependStyle(selector, join);
    }

    // public renderWrap() {
    //     Renderer.renderTo(this.selector || Renderer.selector, this)
    // }

    public renderWrapString(): string {
        return `<${this.wrapElement} id="${this.selector}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`
        // if (!this.selector) {
        //     return Handlebars.compile(this.template)(this);
        // } else if (this.selector.startsWith('.')) {
        //     return `<${this.wrapElement} class="${this.selector.replace('.', ' ')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // } else if (this.selector.startsWith('#')) {
        //     return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // } else {
        //     return `<${this.wrapElement} id="${this.selector.replace('#', '')}">${Handlebars.compile(this.template)(this)}</${this.wrapElement}>`;
        // }
    }

    public exist(): boolean {
        return Renderer.exist(this.selector)
    }

    public toString(): string {
        return this.renderWrapString()
    }
}
