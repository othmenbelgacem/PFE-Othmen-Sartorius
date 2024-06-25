import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionPresencesComponent } from './session-presences.component';

describe('SessionPresencesComponent', () => {
  let component: SessionPresencesComponent;
  let fixture: ComponentFixture<SessionPresencesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SessionPresencesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SessionPresencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
