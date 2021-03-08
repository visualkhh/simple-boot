import Handlebars from 'handlebars'

export class Module {
    public render(obj?: any): string {
        return Handlebars.compile(this.template)(obj ?? this)
    }

    public onInit() {}
    public onChangeRendered() {}

    get template(): string {
        return ''
    }
}
