import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketCatalogItemComponent } from './ticket-catalog-item.component';

describe('TicketCatalogItemComponent', () => {
  let component: TicketCatalogItemComponent;
  let fixture: ComponentFixture<TicketCatalogItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketCatalogItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketCatalogItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
