import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerTrainingRequestComponent } from './manager-training-request.component';

describe('ManagerTrainingRequestComponent', () => {
  let component: ManagerTrainingRequestComponent;
  let fixture: ComponentFixture<ManagerTrainingRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagerTrainingRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerTrainingRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
