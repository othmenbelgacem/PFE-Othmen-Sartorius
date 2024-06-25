import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PresenceSheetComponent } from './presence-sheet.component';

describe('PresenceSheetComponent', () => {
  let component: PresenceSheetComponent;
  let fixture: ComponentFixture<PresenceSheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PresenceSheetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PresenceSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
